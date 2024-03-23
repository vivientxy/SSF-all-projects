package nus.iss.day17weatherapi.model;

import java.io.Serializable;

import jakarta.json.JsonNumber;

public record Weather(String main, String description, String icon, JsonNumber temp) implements Serializable {
    
}
