package ibf2023.ssf.day12workshop.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ibf2023.ssf.day12workshop.ShoppingCart;

@Controller
@RequestMapping(path = "/cart")
public class ShoppingCartController {

    @GetMapping
    public String getCartString() {
        System.out.println("getCartString invoked");
        return "index";
    }

    @GetMapping(path = "{cart}")
    public ModelAndView getCartData(@RequestParam String existingCart, @RequestParam MultiValueMap<String, String> cartInput) {

        ModelAndView mav = new ModelAndView();
        List<ShoppingCart> updatedCart = new LinkedList<>();

        System.out.println("====================================");
        for (String cartKey : cartInput.keySet()) {
            System.out.printf("key: %s, values: %s\n",
            cartKey, cartInput.get(cartKey)); // this get (instead of getFirst) means you get a list of values! for multi-selection
        }
        String newItem = cartInput.getFirst("item");
        int newQuantity = Integer.parseInt(cartInput.getFirst("quantity"));
        
        mav.setViewName("index");
        mav.addObject("shoppingCart", updatedCart);
        mav.setStatus(HttpStatusCode.valueOf(200));
        return mav;
    }



    // @GetMapping(path = "/cart")
    // public ModelAndView getCart(@RequestParam MultiValueMap<String, String> cartLine) {
    //     System.out.println("mav getCart invoked");

    //     ModelAndView mav = new ModelAndView();
    //     List<ShoppingCart> carts = new LinkedList<>();
        
    //     // printing only
    //     System.out.println("====================================");
    //     for (String cartItem : cartLine.keySet()) {
    //         System.out.printf("key: %s, values: %s\n", cartItem, cartLine.get(cartItem)); // this get (instead of getFirst) means you get a list of values! for multi-selection
    //     }

    //     // adding to model
    //     for (int i = 0; i < cartLine.size(); i++) {
    //         // carts.add(new ShoppingCart());
    //     }

    //     mav.setViewName("index");
    //     mav.addObject("shoppingCart", carts);
    //     mav.setStatus(HttpStatusCode.valueOf(200));

    //     return mav;
    // }
}
