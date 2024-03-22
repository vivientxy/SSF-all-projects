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

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    UserService svc;

    @GetMapping(path = {"/"})
    public ModelAndView getIndex(HttpSession sess) {
        ModelAndView mav = new ModelAndView("index");
        Captcha cap = new Captcha();
        if (getAttempts(sess) >= Utils.TRIES_TILL_CAPTCHA) {
            cap.setNum1(Captcha.generate());
            cap.setNum2(Captcha.generate());
        }
        mav.addObject("loginUser", new User());
        mav.addObject("attempts", getAttempts(sess));
        mav.addObject("captcha", cap);
        System.out.println("GET MAPPING");
        return mav;
    }

    @PostMapping(path = {"logout"})
    public ModelAndView processLogout(HttpSession sess) {
        ModelAndView mav = new ModelAndView();
        sess.invalidate();
        mav.setViewName("redirect:/");
        System.out.println("LOGGING OUT");
        return mav;
    }

    @PostMapping(path="/login")
    public ModelAndView getLogin(HttpSession sess, @ModelAttribute User loginUser, BindingResult binding, @ModelAttribute Captcha captcha) {
        // **implement mandatory fields for username, password and captcha?!?!

        System.out.println("POST MAPPING. ATTEMPT NUMBER: " + getAttempts(sess));
        ModelAndView mav = new ModelAndView();

        // if login succeed (with and without captcha):
        if (svc.isPasswordCorrect(loginUser)) {
            if (captcha.isEmpty() || captcha.isCorrect())
                System.out.println("captcha is empty: " + captcha.isEmpty() + captcha.toString());
                System.out.println("captcha is correct: " + captcha.isCorrect());
                mav.setViewName("success");
                return mav;
        }

        // if login fail
        Captcha cap = new Captcha();

        // check for input errors
        if (binding.hasErrors()) {
            mav.setViewName("index");
            mav.addObject("attempts", getAttempts(sess));
            mav.addObject("loginUser", loginUser);
            mav.addObject("captcha", cap);
            return mav;
        }

        int attempts = getAttempts(sess) + 1;

        if (attempts < Utils.TRIES_TILL_CAPTCHA) {
            // display number of attempts left
            mav.setViewName("index");
        } else if (attempts < Utils.TRIES_TILL_SUSPEND) {
            // captcha + display number of attempts left
            cap.setNum1(Captcha.generate());
            cap.setNum2(Captcha.generate());
            mav.setViewName("index");
        } else {
            // suspended
            mav.setViewName("suspended");
        }

        sess.setAttribute("attempts", attempts);
        mav.addObject("attempts", attempts);
        mav.addObject("loginUser", loginUser);
        mav.addObject("captcha", cap);

        System.out.println("captcha is empty: " + captcha.isEmpty() + captcha.toString());
        System.out.println("captcha is correct: " + captcha.isCorrect());
        System.out.println("POST -- FAIL LOGIN. ATTEMPT NUMBER: " + getAttempts(sess) + " AND MAV IS: " + mav.toString() + " AND CAPTCHA IS: " + cap.getNum1() + "," + cap.getNum2());
        return mav;
    }

    private Integer getAttempts(HttpSession sess) {
        Object a = sess.getAttribute("attempts");
        if (null == a) {
            return 0;
        }
        return (Integer)a;
    }

}
