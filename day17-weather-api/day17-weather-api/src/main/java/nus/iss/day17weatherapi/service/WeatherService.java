package nus.iss.day17weatherapi.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Value;
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

@Service
public class WeatherService {

    private static final String SEARCH_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String ICON_URL_START = "https://openweathermap.org/img/wn/";
    private static final String ICON_URL_END = "@2x.png";

    @Value("${api.key}")
    private String apiKey;

    public Weather getWeatherAPI(String citySearch) {
        String weatherUrl = UriComponentsBuilder
                .fromUriString(SEARCH_URL)
                .queryParam("q", citySearch)
                .queryParam("appid", apiKey)
                .queryParam("units", "metric")
                .toUriString();

        // I SEARCH THIS
        RequestEntity<Void> req = RequestEntity.get(weatherUrl).build();

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

    public String getIconUrl(String iconId) {
        StringBuilder builder = new StringBuilder();
        builder.append(ICON_URL_START);
        builder.append(iconId);
        builder.append(ICON_URL_END);
        return builder.toString();
    }

}
