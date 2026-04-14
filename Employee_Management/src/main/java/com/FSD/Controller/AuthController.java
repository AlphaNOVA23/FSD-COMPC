package com.FSD.Controller;

import com.FSD.Entity.CredentialEntity;
import com.FSD.Entity.EmployeeEntity;
import com.FSD.Entity.LoginDetailsEntity;
import com.FSD.Repository.CredentialRepository;
import com.FSD.Repository.EmployeeRepository;
import com.FSD.Repository.LoginDetailsRepository;
import com.FSD.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> payload) {
        String username = payload.get("email");
        String password = payload.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        // Extract the role from authorities
        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        String token = jwtUtil.generateToken(userDetails, role);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        String name = payload.get("name");

        if (email == null || password == null || name == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Name, email, and password are strictly required for registration."));
        }

        // 1. Identity Verification
        if (loginDetailsRepository.findByUsername(email) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email is already permanently registered."));
        }

        // 2. Entity Creation 1: The Human (EmployeeEntity)
        EmployeeEntity newEmployee = new EmployeeEntity(name);
        EmployeeEntity savedEmployee = employeeRepository.save(newEmployee);

        // 3. Entity Creation 2: The Keys (CredentialEntity)
        CredentialEntity newCredential = new CredentialEntity(savedEmployee);
        CredentialEntity savedCredential = credentialRepository.save(newCredential);

        // 4. Entity Creation 3: The Account (LoginDetailsEntity)
        LoginDetailsEntity newLogin = new LoginDetailsEntity();
        newLogin.setCredential(savedCredential);
        newLogin.setUsername(email);
        newLogin.setPasswordHash(passwordEncoder.encode(password));
        newLogin.setRole("ROLE_USER");
        loginDetailsRepository.save(newLogin);

        // 5. Automatic Login Flow (Bypass frontend login page)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails, "ROLE_USER");

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Registration completely successful.");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
