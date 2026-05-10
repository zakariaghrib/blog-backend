package com.Zakaria.blog_backend.controllers;

import com.Zakaria.blog_backend.models.User;
import com.Zakaria.blog_backend.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Permet à ton futur frontend de communiquer avec l'API sans blocage
public class UserController {

    private final UserRepository userRepository;

    // L'injection de dépendance : Spring Boot fournit automatiquement le Repository ici
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Route pour récupérer tous les utilisateurs (GET http://localhost:8080/api/users)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Route pour créer un nouvel utilisateur (POST http://localhost:8080/api/users)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}