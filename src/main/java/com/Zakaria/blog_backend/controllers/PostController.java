package com.Zakaria.blog_backend.controllers;

import com.Zakaria.blog_backend.models.Post;
import com.Zakaria.blog_backend.repositories.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Route pour récupérer tous les articles
    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Route pour créer un nouvel article
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }
    // Route pour modifier un article existant
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        return postRepository.findById(id)
                .map(post -> {
                    post.setTitle(postDetails.getTitle());
                    post.setContent(postDetails.getContent());
                    // On sauvegarde les modifications
                    return postRepository.save(post);
                }).orElseThrow(() -> new RuntimeException("Article introuvable avec l'ID : " + id));
    }

    // Route pour supprimer un article
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "L'article a été supprimé avec succès !";
    }
}