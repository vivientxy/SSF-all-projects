package ibf2023.ssf.day12.controller;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = "/pizza")
public class PizzaController {

    @GetMapping
    public String getPizza(@RequestParam MultiValueMap<String, String> orderForm) {

        System.out.println("====================================");
        for (String ctrlName : orderForm.keySet()) {
            System.out.printf("key: %s, values: %s\n",
            ctrlName, orderForm.get(ctrlName)); // this get (instead of getFirst) means you get a list of values! for multi-selection
        }
        return "redirect:/";
    }
}
