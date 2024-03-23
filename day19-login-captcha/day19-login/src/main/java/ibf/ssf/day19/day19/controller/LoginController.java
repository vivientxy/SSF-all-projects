package ibf.ssf.day19.day19.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ibf.ssf.day19.day19.model.Captcha;
import ibf.ssf.day19.day19.model.User;
import ibf.ssf.day19.day19.service.UserService;
import ibf.ssf.day19.day19.util.Utils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    UserService svc;

    @GetMapping(path = { "/" })
    public ModelAndView getIndex(HttpSession sess) {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", new User());
        mav.addObject("attempts", getAttempts(sess));
        mav.addObject("captcha", new Captcha());
        System.out.println("GET MAPPING");
        return mav;
    }

    /*
     * LOGOUT AND RESET COOKIES
     */

    @PostMapping(path = { "logout" })
    public ModelAndView processLogout(HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        sess.invalidate();
        mav.setViewName("redirect:/");
        System.out.println("LOGGING OUT");
        return mav;
    }

    /*
     * LOGIN TO ACCOUNT
     */

    @PostMapping(path = "/login")
    public ModelAndView getLogin(HttpSession sess, @ModelAttribute Captcha captcha, @ModelAttribute @Valid User user,
            BindingResult bindingUser) {
        ModelAndView mav = new ModelAndView("index");
        System.out.println("POST MAPPING. ATTEMPT NUMBER: " + getAttempts(sess));

        // check for suspension
        if (getAttempts(sess) > Utils.TRIES_TILL_SUSPEND) {
            mav.setViewName("suspended");
            return mav;
        }

        // check for input errors -- return to page without ++ attempts
        if (bindingUser.hasErrors()) {
            mav.addObject("user", user);
            mav.addObject("attempts", getAttempts(sess));
            mav.addObject("captcha", new Captcha()); // give new Captcha
            return mav;
        }

        // if login succeed (with and without captcha):
        if (svc.isPasswordCorrect(user)) {
            System.out.println("Is captcha enabled?: " + isCaptchaEnabled(sess));
            System.out.println("Is captcha correct?: " + captcha.isCorrect());
            if (!isCaptchaEnabled(sess) || captcha.isCorrect()) {
                mav.setViewName("success");
                return mav;
            }
        }

        // if login fail
        int attempts = getAttempts(sess) + 1;
        sess.setAttribute("attempts", attempts);

        // suspended
        if (attempts >= Utils.TRIES_TILL_SUSPEND)
            mav.setViewName("suspended");

        System.out
                .println("POST -- FAIL LOGIN. ATTEMPT NUMBER: " + getAttempts(sess) + " AND MAV IS: " + mav.toString());

        mav.addObject("attempts", attempts);
        mav.addObject("user", user);
        mav.addObject("captcha", new Captcha());
        return mav;
    }

    private Integer getAttempts(HttpSession sess) {
        Object a = sess.getAttribute("attempts");
        if (null == a)
            return 0;
        return (Integer) a;
    }

    private Boolean isCaptchaEnabled(HttpSession sess) {
        if (getAttempts(sess) >= Utils.TRIES_TILL_CAPTCHA)
            return true;
        return false;
    }

    /*
     * FORGET PASSWORD
     */

    @GetMapping(path = { "/reset" })
    public ModelAndView getForgetPassword(HttpSession sess) {
        ModelAndView mav = new ModelAndView("reset");
        mav.addObject("user", new User());
        mav.addObject("captcha", new Captcha());
        System.out.println("GET RESET");
        return mav;
    }

    @PostMapping(path = { "/reset" })
    public ModelAndView getSecurityQuestion(HttpSession sess, @ModelAttribute Captcha captcha,
            @ModelAttribute User user,
            BindingResult bindings) {
        System.out.println("POST RESET");
        ModelAndView mav = new ModelAndView("reset");
        mav.addObject("user", user);
        mav.addObject("captcha", new Captcha());

        // check if username exists
        if (!svc.doesUserExist(user)) {
            // user doesn't exist
            ObjectError error = new ObjectError("user", "User does not exist!");
            bindings.addError(error);
            return mav;
        }

        // check if captcha correct
        if (!captcha.isCorrect()) {
            ObjectError error = new ObjectError("captcha", "Please enter captcha");
            bindings.addError(error);
            return mav;
        }

        // ok to proceed to reset2
        // grab security question and inject into model for next page
        mav.setViewName("reset2");
        String securityQuestion = svc.retrieveUser(user.getUsername()).getSecurityQuestion();
        user.setSecurityQuestion(securityQuestion);
        mav.addObject("user", user);
        System.out.println("user after successfully entering username and captcha: " + user.toString());
        return mav;
    }

    @PostMapping(path = { "/resetdone" })
    public ModelAndView checkSecurityQuestion(HttpSession sess, @ModelAttribute @Valid User user,
            BindingResult bindings, @RequestParam @NotBlank String confirmPassword) {
        ModelAndView mav = new ModelAndView("reset2");

        mav.addObject("user", user);

        System.out.println("RESETCHECK STAGE");
        System.out.println("any errors: " + bindings.toString());
        System.out.println("requestparam user: " + user.toString());
        System.out.println("confirm password is null: " + (""==confirmPassword));
        System.out.println("confirm password: " + confirmPassword);

        // check for input errors
        if (bindings.hasErrors()) {
            return mav;
        }

        // check for security answer
        if (!svc.isSecurityAnswerCorrect(user)) {
            ObjectError error = new ObjectError("securityAnswer", "Security answer is incorrect");
            bindings.addError(error);
            return mav;
        }

        // check confirmPassword matches newPassword
        String newPassword = user.getPassword();
        if (!newPassword.equals(confirmPassword)) {
            ObjectError error = new ObjectError("confirmPassword", "Confirm password does not match new password");
            bindings.addError(error);
            return mav;
        }

        // success!
        svc.updateUserPassword(user.getUsername(), newPassword);
        mav.setViewName("changed-password");
        return mav;
    }

    /*
     * REGISTER NEW ACCOUNT
     */

    @GetMapping(path = { "/register" })
    public ModelAndView getRegister(HttpSession sess, @ModelAttribute @Valid User user, BindingResult bindings) {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("user", new User());
        return mav;
    }

    @PostMapping(path = { "/register" })
    public ModelAndView processRegister(HttpSession sess, @ModelAttribute @Valid User user, BindingResult bindings) {
        ModelAndView mav = new ModelAndView("register");
        if (bindings.hasErrors()) {
            mav.addObject("user", user);
            return mav;
        }
        if (!svc.createUser(user)) {
            // user already exists
            ObjectError error = new ObjectError("globalError", "User already exists! Please choose another username");
            bindings.addError(error);
            mav.addObject("user", user);
            return mav;
        }
        mav.setViewName("created-user");
        return mav;
    }

}
