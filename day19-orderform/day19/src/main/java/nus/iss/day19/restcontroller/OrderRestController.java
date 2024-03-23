package nus.iss.day19.restcontroller;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import nus.iss.day19.model.Item;

@RestController
public class OrderRestController {

    @PostMapping(path = "/api/order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postTimeAsJsonString(@RequestBody String payload) {
        JsonReader jReader = Json.createReader(new StringReader(payload));
        JsonObject jObject = jReader.readObject();
        System.out.println("payload: " + jObject.toString());

        String name = jObject.getString("name");
        String email = jObject.getString("email");
        Boolean rush = jObject.getBoolean("rush");
        String comments = jObject.getString("comments");
        JsonArray jItems = jObject.getJsonArray("items");
        List<Item> itemList = new LinkedList<>();
        for (JsonValue jsonValue : jItems) {
            JsonObject j = jsonValue.asJsonObject();
            Item item = new Item(j.getString("item"), j.getInt("quantity"));
            itemList.add(item);
        }

        System.out.println("=====================================================");
        System.out.println("name: " + name);
        System.out.println("email: " + email);
        System.out.println("rush: " + rush);
        System.out.println("comments: " + comments);
        for (Item item : itemList) {
            System.out.println("item: " + item.toString());
        }
        System.out.println("=====================================================");

        // what do i need to verify?? -- BUSINESS LOGIC
        // arbitrary stock count
        // need to normalise the keys to make sure both lists match
        // else return error saying "we do not have that in stock"
        Map<String, Integer> stockMap = new HashMap<>();
        stockMap.putIfAbsent("apple", 10);
        stockMap.putIfAbsent("orange", 10);
        stockMap.putIfAbsent("pear", 10);
        stockMap.putIfAbsent("kiwi", 10);
        stockMap.putIfAbsent("mango", 10);
        stockMap.putIfAbsent("watermelon", 10);
        stockMap.putIfAbsent("strawberry", 10);

        JsonObject responsePayload = null;
        if (hasEnoughStock(itemList, stockMap)) {
            responsePayload = Json.createObjectBuilder()
                    .add("orderId", UUID.randomUUID().toString())
                    .build();
            return new ResponseEntity<String>(responsePayload.toString(), HttpStatus.ACCEPTED);
        } else {
            responsePayload = Json.createObjectBuilder()
                    .add("message", whatIsOOS(itemList, stockMap))
                    .build();
            return new ResponseEntity<String>(responsePayload.toString(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    private Boolean hasEnoughStock(List<Item> orderList, Map<String, Integer> stockMap) {
        // need to normalise the keys to make sure both lists match
        for (Item item : orderList) {
            String itemName = item.item().toLowerCase().replaceAll("\\s+", "");
            if (stockMap.containsKey(itemName)) {
                // check qty sufficient
                if (stockMap.get(itemName) < item.quantity()) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private String whatIsOOS(List<Item> orderList, Map<String, Integer> stockMap) {
        StringBuilder builder = new StringBuilder();
        for (Item item : orderList) {
            String itemName = item.item().toLowerCase().replaceAll("\\s+", "");
            if (stockMap.containsKey(itemName)) {
                // check qty sufficient
                if (stockMap.get(itemName) < item.quantity()) {
                    builder.append(item.item() + ": we only have " + stockMap.get(itemName) + " available. ");
                }
            } else {
                // item does not exist
                builder.append(item.item() + ": we do not sell this. ");
            }
        }
        return builder.toString();
    }

}
