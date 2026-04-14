package com.FSD.Security;

import com.FSD.Entity.LoginDetailsEntity;
import com.FSD.Repository.LoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PasswordHashSeeder implements CommandLineRunner {

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Checking Database Passwords for bcrypt Hashes ---");
        List<LoginDetailsEntity> allLogins = loginDetailsRepository.findAll();

        for (LoginDetailsEntity login : allLogins) {
            String currentPassword = login.getPasswordHash();
            
            // If the password doesn't start with the bcrypt identifier, automatically hash it!
            if (currentPassword != null && !currentPassword.startsWith("$2a$")) {
                login.setPasswordHash(passwordEncoder.encode(currentPassword));
                loginDetailsRepository.save(login);
                System.out.println("Dynamically hashed password for user: " + login.getUsername());
            }
        }
    }
}
