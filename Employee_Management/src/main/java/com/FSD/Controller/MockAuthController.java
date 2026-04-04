package com.FSD.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MockAuthController {

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> mockLogin(@RequestBody Map<String, Object> payload) {
        System.out.println("MOCK LOGIN REQUEST RECEIVED: " + payload);
        Map<String, String> response = new HashMap<>();
        response.put("token", "dummy-jwt-token-from-spring");
        return ResponseEntity.ok(response);
    }   

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> mockRegister(@RequestBody Map<String, Object> payload) {
        System.out.println("MOCK REGISTER REQUEST RECEIVED: " + payload);
        Map<String, String> response = new HashMap<>();
        response.put("token", "dummy-jwt-token-from-spring");
        return ResponseEntity.ok(response);
    }
}
