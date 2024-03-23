package ssf.day12workshopretry.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import ssf.day12workshopretry.models.Item;

@Controller
@RequestMapping
public class PostCartController {

    @GetMapping(path = {"/","/index.html"})
    public ModelAndView getIndex(HttpSession sess) {
        ModelAndView mav = new ModelAndView("cart");
        List<Item> cart = getCart(sess);

        mav.addObject("item", new Item());
        mav.addObject("cart", cart);

        return mav;
    }

    @PostMapping(path = "/checkout")
    public String postCheckout(HttpSession sess) {
        List<Item> cart = getCart(sess);

        System.out.println(cart);

        // perform checkout

        // destroy session (for the next request, not this request)
        sess.invalidate();

        return "thankyou";
    }

    
    @PostMapping(path = "/cart2")
    public ModelAndView postCart2(HttpSession sess, @ModelAttribute @Valid Item item, BindingResult bindings) {
        ModelAndView mav = new ModelAndView("cart");
        
        List<Item> cart = getCart(sess);
        mav.addObject("cart", cart);

        // syntactic validation
        if (bindings.hasErrors()) {
            mav.addObject("item", item);
        }

        // semantic validation
        else if ("apple".equals(item.getName())) {
            FieldError err = new FieldError("item", "name", "We don't sell apples here");
            bindings.addError(err);
            mav.addObject("item", item);

            // ObjectError objErr = new ObjectError("globalError", "This error belongs to the form");
            // bindings.addError(objErr);
        }

        else {
            mav.addObject("item", new Item());
            cart.add(item);
        }

        System.out.println(item);
        return mav;
    }

    @SuppressWarnings("unchecked")
    private List<Item> getCart(HttpSession sess) {
        List<Item> cart = (List<Item>)sess.getAttribute("cart");
        // check if cart exists. if no, then this is a new session (initialize)
        if (null == cart) {
            cart = new LinkedList<>();
            sess.setAttribute("cart", cart);
        }
        return cart;
    }

}
