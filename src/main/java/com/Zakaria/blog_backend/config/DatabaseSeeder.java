package com.Zakaria.blog_backend.config;

import com.Zakaria.blog_backend.models.Post;
import com.Zakaria.blog_backend.models.User;
import com.Zakaria.blog_backend.repositories.PostRepository;
import com.Zakaria.blog_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // On vérifie si la base de données est vide (0 utilisateur)
        if (userRepository.count() == 0) {
            System.out.println("🌱 Base de données vierge détectée : Insertion des données officielles...");

            // 1. Création de ton compte officiel
            User admin = new User();
            admin.setUsername("Zakaria Ghrib");
            admin.setEmail("zakaria.ghrib@portfolio.ma"); // Tu pourras te connecter avec ça
            // Ton mot de passe officiel (il sera crypté en BDD)
            admin.setPassword(passwordEncoder.encode("Zakaria2026!"));

            userRepository.save(admin);

            // 2. Création de ton premier article de présentation
            Post firstPost = new Post();
            firstPost.setTitle("Bienvenue sur mon Portfolio DevBlog");
            firstPost.setContent("Bonjour et bienvenue ! \n\nJe suis Zakaria, Développeur Full-Stack spécialisé dans les écosystèmes MERN et Spring Boot. \n\nJ'ai développé cette application de blog de A à Z. Le frontend est construit avec React et Tailwind CSS pour une interface fluide, tandis que le backend repose sur une architecture robuste en Java Spring Boot, sécurisée avec des tokens JWT et connectée à une base de données PostgreSQL.\n\nN'hésitez pas à vous créer un compte pour tester les fonctionnalités de commentaires !");
            firstPost.setAuthor(admin);

            postRepository.save(firstPost);

            System.out.println("✅ Données officielles insérées avec succès !");
        }
    }
}