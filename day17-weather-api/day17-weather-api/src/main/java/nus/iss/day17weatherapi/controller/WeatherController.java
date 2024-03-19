package nus.iss.day17weatherapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nus.iss.day17weatherapi.model.Weather;
import nus.iss.day17weatherapi.service.WeatherService;

@Controller
@RequestMapping
public class WeatherController {

    @Autowired
    WeatherService svc;

    @PostMapping("/weather")
    public ModelAndView displayWeather(@RequestParam String city) {
        ModelAndView mav = new ModelAndView("weather");
        Weather weather = svc.getWeatherAPI(city);
        String iconUrl = svc.getIconUrl(weather.icon());
        mav.addObject("weather", weather);
        mav.addObject("city", city);
        mav.addObject("iconUrl", iconUrl);
        return mav;
    }
    
}
