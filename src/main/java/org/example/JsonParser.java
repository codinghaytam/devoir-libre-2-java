package org.example;


import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonParser {
    public Order JsonToOrder(String json) throws Exception {

            // JSON string representing an array of user objects

            // Create an ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();

            // Deserialize the JSON string into an array of User objects
            return objectMapper.readValue(json, Order.class);

    }
}
