package nus.iss.day17weatherapi.repo;

import java.io.StringReader;
import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import nus.iss.day17weatherapi.model.Weather;

@Repository
public class WeatherRepo {

    @Autowired @Qualifier("myredis")
    RedisTemplate<String, String> template;

    public void cache(String city, Weather weather) {
        String _key = normalizeKey(city);
        String _value = toJson(weather);
        ValueOperations<String,String> ops = template.opsForValue();
        // cache for 30mins
        ops.set(_key, _value, Duration.ofMinutes(30));
    }

    private String normalizeKey(String city) {
        return city.toLowerCase().replaceAll("\\s+", "");
    }

    private String toJson(Weather weather) {
        return Json.createObjectBuilder()
                .add("main", weather.main())
                .add("description", weather.description())
                .add("icon", weather.icon())
                .add("temp", weather.temp())
                .build()
                .toString();
    }

    // public Optional<List<Weather>> get(String city) {
    //     ValueOperations<String, String> ops = template.opsForValue();
    //     String _key = normalizeKey(city);
    //     String _value = ops.get(_key);
    //     if (null == _value) {
    //         return Optional.empty();
    //     }
    //     return Optional.of(toWeather(_value));
    // }

    public Optional<Weather> get(String city) {
        ValueOperations<String, String> ops = template.opsForValue();
        String _key = normalizeKey(city);
        String _value = ops.get(_key);
        if (null == _value) {
            return Optional.empty();
        }
        return Optional.of(toWeather(_value));
    }

    // private List<Weather> toWeather(String str) {
    //     JsonReader jReader = Json.createReader(new StringReader(str));
    //     JsonArray jArr = jReader.readArray();
    //     List<Weather> weather = new LinkedList<>();
    //     for (JsonValue jsonValue : jArr) {
    //         weather.add(toWeather(jsonValue.asJsonObject()));
    //     }
    //     return weather;
    // }

    private Weather toWeather(String str) {
        JsonReader jReader = Json.createReader(new StringReader(str));
        JsonObject jObject = jReader.readObject();
        return toWeather(jObject);
    }

    private Weather toWeather(JsonObject j) {
        return new Weather(j.getString("name"), j.getString("description"), j.getString("icon"), j.getJsonNumber("temp"));
    }

}
