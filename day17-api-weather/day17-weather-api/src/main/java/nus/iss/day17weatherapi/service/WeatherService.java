package nus.iss.day17weatherapi.service;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.day17weatherapi.model.Weather;
import nus.iss.day17weatherapi.repo.WeatherRepo;

@Service
public class WeatherService {

    private static final String SEARCH_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Value("${api.key}")
    private String apiKey;

    @Autowired
    WeatherRepo weatherRepo;

    public Weather getWeatherAPI(String citySearch) {
        // // if do chuk's way and your method returns List<Weather>:
        // Optional<List<Weather>> opt = weatherRepo.get(citySearch);
        // if (opt.isPresent()) {
        //     return opt.get();
        // }

        Optional<Weather> opt = weatherRepo.get(citySearch);
        if (opt.isPresent()) {
            return opt.get();
        }

        String weatherUrl = UriComponentsBuilder
                .fromUriString(SEARCH_URL)
                .queryParam("q", citySearch.replace(" ", "+"))
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        // I SEARCH THIS
        RequestEntity<Void> req = RequestEntity
            .get(weatherUrl)    // VERB(NOUN) -- your HTTP request line!
            .accept(MediaType.APPLICATION_JSON)
            .build();

        RestTemplate template = new RestTemplate();

        // I GET THIS BACK AS RESPONSE
        ResponseEntity<String> resp = template.exchange(req, String.class);
        // THIS IS ONE LONG STRING. SO I WANT TO PROCESS IT (RECALL THE TREE)
        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        JsonObject pageReply = reader.readObject();
        JsonArray weatherElement = pageReply.getJsonArray("weather");
        JsonObject element = weatherElement.getJsonObject(0);
        String main = element.getString("main");
        String description = element.getString("description");
        String icon = element.getString("icon");
        // pageReply.getJsonObject("main").get
        JsonNumber temp = pageReply.getJsonObject("main").getJsonNumber("temp");
        Weather weather = new Weather(main, description, icon, temp);

        return weather;
    }

}
