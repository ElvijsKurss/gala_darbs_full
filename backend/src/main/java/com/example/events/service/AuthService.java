package com.example.events.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.events.dto.AuthResponse;
import com.example.events.dto.LoginRequest;
import com.example.events.dto.RegisterRequest;
import com.example.events.entity.User;
import com.example.events.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException("Lietotājvārds ir obligāts");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Parole ir obligāta");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Šāds lietotājvārds jau eksistē");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return new AuthResponse("Reģistrācija veiksmīga", user.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new RuntimeException("Lietotājvārds ir obligāts");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("Parole ir obligāta");
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Lietotājs nav atrasts"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Nepareiza parole");
        }

        return new AuthResponse("Pieslēgšanās veiksmīga", user.getUsername());
    }
}