package com.Zakaria.blog_backend.controllers;

import com.Zakaria.blog_backend.models.User;
import com.Zakaria.blog_backend.repositories.UserRepository;
import com.Zakaria.blog_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Récupérer tous les utilisateurs (Optionnel, mais on le garde)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 1. INSCRIPTION : Hachage du mot de passe
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Vérifier si l'email existe déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Cet email est déjà utilisé.");
        }

        // Crypter le mot de passe avant la sauvegarde
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    // 2. CONNEXION : Vérification et génération du Token JWT
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            // Spring Security vérifie si l'email et le mot de passe (non haché tapé par l'utilisateur) correspondent au hachage en BDD
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            // Si on arrive ici, les identifiants sont bons. On récupère l'utilisateur.
            User user = userRepository.findByEmail(loginRequest.getEmail()).get();

            // On fabrique le fameux Token JWT
            String token = jwtUtil.generateToken(user.getEmail());

            // On prépare la réponse pour React : on lui envoie le Token ET les infos de l'utilisateur
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Si les identifiants sont faux, on renvoie une erreur 401
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect.");
        }
    }
}