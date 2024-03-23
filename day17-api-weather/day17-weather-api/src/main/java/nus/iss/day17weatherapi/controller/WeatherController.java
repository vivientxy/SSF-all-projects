package nus.iss.day17weatherapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import nus.iss.day17weatherapi.model.Weather;
import nus.iss.day17weatherapi.service.HttpbinService;
import nus.iss.day17weatherapi.service.WeatherService;

@Controller
@RequestMapping
public class WeatherController {

    @Autowired
    WeatherService svc;


    @Autowired
    HttpbinService httpbinSvc;

    @PostMapping("/weather")
    public ModelAndView displayWeather(HttpSession sess, @RequestParam String city) {
        ModelAndView mav = new ModelAndView("weather");
        Weather weather;
        if (sess.getAttribute(city) == null) {
            weather = svc.getWeatherAPI(city);
            sess.setAttribute(city, weather);
            sess.setMaxInactiveInterval(1800);
        } else {
            weather = (Weather)sess.getAttribute(city);
        }

        // Weather weather = svc.getWeatherAPI(city);
        mav.addObject("weather", weather);
        mav.addObject("city", city);
        return mav;
    }

    @GetMapping("/healthzMav")
    public ModelAndView getHealthz() {
        ModelAndView mav = new ModelAndView();
        if (httpbinSvc.isAlive()) {
            mav.setStatus(HttpStatusCode.valueOf(200));
        } else {
            mav.setStatus(HttpStatusCode.valueOf(400));
        }
        return mav;
    }

    @GetMapping("/healthzJson")
    @ResponseBody
    public ResponseEntity<String> getHealthzUsingJson() {
        JsonObject j = Json.createObjectBuilder().build();
        if (httpbinSvc.isAlive()) {
            return ResponseEntity.ok(j.toString());
        } else {
            return ResponseEntity.status(400).body(j.toString());
        }
    }
    
}
