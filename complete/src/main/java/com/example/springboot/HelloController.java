package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    private Map<String, String> database = new HashMap<>();

    public HelloController() {
        database.put("greeting", "Hello, welcome to Google Cloud Next Tokyo 24!");
    }

    private String generateHtml(String title, String content, String linkText, String linkHref) {
        return "<html><body style='font-family: Arial, sans-serif; text-align: center; display: flex; flex-direction: column; justify-content: center; align-items: center; min-height: 100vh;'>" +
               "<div style='padding: 20px; border: 1px solid #ccc; border-radius: 5px; background-color: #f9f9f9;'>" +
               "<h1 style='color: #2196F3;'>" + title + "</h1>" +
               "<p>" + content + "</p>" +
               (linkText != null ? "<p><a href='" + linkHref + "'>" + linkText + "</a></p>" : "") + 
               "</div></body></html>";
    }

    @GetMapping("/")
    public String index() {
        return generateHtml("Google Cloud Next Tokyo 24", database.get("greeting"), "More Info", "/api/info");
    }

    @GetMapping("/api/info")
    public String info() {
        return generateHtml("Information Page", "This is a sample application for the Google Cloud Next Tokyo 24 event.", "Go Back", "/");
    }

    @GetMapping("/api/external")
    public ResponseEntity<String> externalAPI() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.example.com/data"; 
            String result = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok(generateHtml("External API Data", "Data from external API: " + result, "Go Back", "/"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateHtml("Error", "Failed to fetch data from external API.", "Go Back", "/"));
        }
    }

    @GetMapping("/api/data/{key}")
    public ResponseEntity<String> getData(@PathVariable String key) {
        String value = database.get(key);
        if (value != null) {
            return ResponseEntity.ok(generateHtml("Data for key: " + key, "Value: " + value, "Go Back", "/"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(generateHtml("Error", "Key not found: " + key, "Go Back", "/"));
        }
    }

    @GetMapping("/api/data")
    public ResponseEntity<String> setData(@RequestParam String key, @RequestParam String value) {
        database.put(key, value);
        return ResponseEntity.ok(generateHtml("Data Saved", "Key: " + key + "<br>Value: " + value, "Go Back", "/"));
    }
}
