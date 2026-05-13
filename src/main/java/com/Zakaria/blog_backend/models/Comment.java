package com.Zakaria.blog_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Relation : Un commentaire appartient à un article (Plusieurs commentaires pour un Post)
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // Relation : Un commentaire est écrit par un utilisateur (Plusieurs commentaires par User)
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Constructeur vide obligatoire pour Hibernate
    public Comment() {
    }

    // --- GETTERS ET SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}