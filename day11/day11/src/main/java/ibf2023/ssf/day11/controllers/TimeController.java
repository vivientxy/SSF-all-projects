package ibf2023.ssf.day11.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path="/time") // HANDLES RESOURCES /time
public class TimeController {

    @GetMapping(path={"/dayofweek","/dow"}) // HANDLES SUB-RESOURCES /time/dayofweek or (path={"/dayofweek","/dow"}) or (path="/dayofweek")
    public String getDayOfWeek(Model model) {
        DateFormat formatter = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayofweek = formatter.format(date);

        model.addAttribute("dow", dayofweek);

        return "dayofweek";
    }

    
    @GetMapping // HANDLES GET REQUESTS
    public String getTime(Model model) {
        System.out.println(">>>> GET /time");
        String currTime = (new Date()).toString();

        model.addAttribute("time", currTime);

        return "time"; // this will return the HTML page named "time" found in templates
    }
}
