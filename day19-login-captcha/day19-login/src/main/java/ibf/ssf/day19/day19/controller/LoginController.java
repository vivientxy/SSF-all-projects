package ibf.ssf.day19.day19.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ibf.ssf.day19.day19.model.Captcha;
import ibf.ssf.day19.day19.model.User;
import ibf.ssf.day19.day19.service.UserService;
import ibf.ssf.day19.day19.util.Utils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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

    @PostMapping(path = { "logout" })
    public ModelAndView processLogout(HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        sess.invalidate();
        mav.setViewName("redirect:/");
        System.out.println("LOGGING OUT");
        return mav;
    }

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

        //suspended
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

}
