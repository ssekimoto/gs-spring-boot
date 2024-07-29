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
        // 初期データ
        database.put("greeting", "Hello, welcome to Google Cloud Next Tokyo 24!");
    }

    @GetMapping("/")
    public String index() {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h1 style='color: #2196F3;'>Google Cloud Next Tokyo 24</h1>" +
                "<p>" + database.get("greeting") + "</p>" +
                "<p><a href='/api/info'>More Info</a></p>" +
                "</body></html>";
    }

    @GetMapping("/api/info")
    public String info() {
        return "<html><body style='font-family: Arial, sans-serif;'>" +
                "<h1 style='color: #4CAF50;'>Information Page</h1>" +
                "<p>This is a sample application for the Google Cloud Next Tokyo 24 event.</p>" +
				"<p><a href='/'>Go Back</a></p>" +
                "</body></html>";
    }

    @GetMapping("/api/external")
    public ResponseEntity<String> externalAPI() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.example.com/data"; 
            String result = restTemplate.getForObject(url, String.class);
            return ResponseEntity.ok("<html><body style='font-family: Arial, sans-serif;'>" +
                    "<h1 style='color: #FF9800;'>External API Data</h1>" +
                    "<p>Data from external API: " + result + "</p>" +
                    "<p><a href='/'>Go Back</a></p>" +
                    "</body></html>");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<h1 style='color: #F44336;'>Error</h1>" +
                    "<p>Failed to fetch data from external API.</p>" +
                    "<p><a href='/'>Go Back</a></p>" +
                    "</body></html>");
        }
    }

    @GetMapping("/api/data/{key}")
    public ResponseEntity<String> getData(@PathVariable String key) {
        String value = database.get(key);
        if (value != null) {
            return ResponseEntity.ok("<html><body style='font-family: Arial, sans-serif;'>" +
                    "<h1 style='color: #3F51B5;'>Data for key: " + key + "</h1>" +
                    "<p>Value: " + value + "</p>" +
                    "<p><a href='/'>Go Back</a></p>" +
                    "</body></html>");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<h1 style='color: #F44336;'>Error</h1>" +
                    "<p>Key not found: " + key + "</p>" +
                    "<p><a href='/'>Go Back</a></p>" +
                    "</body></html>");
        }
    }

    @GetMapping("/api/data")
    public ResponseEntity<String> setData(@RequestParam String key, @RequestParam String value) {
        database.put(key, value);
        return ResponseEntity.ok("<html><body style='font-family: Arial, sans-serif;'>" +
                "<h1 style='color: #4CAF50;'>Data Saved</h1>" +
                "<p>Key: " + key + "</p>" +
                "<p>Value: " + value + "</p>" +
                "<p><a href='/'>Go Back</a></p>" +
                "</body></html>");
    }
}

