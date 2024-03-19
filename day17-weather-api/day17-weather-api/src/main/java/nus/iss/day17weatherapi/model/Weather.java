package nus.iss.day17weatherapi.model;

import jakarta.json.JsonNumber;

public record Weather(String main, String description, String icon, JsonNumber temp) {
    
}
