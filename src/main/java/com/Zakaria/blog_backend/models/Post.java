package com.Zakaria.blog_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    // ⚠️ Le @JsonIgnore a été retiré ici pour que le frontend puisse lire "username"
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    // On garde le @JsonIgnore ici pour éviter une boucle avec les commentaires
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;
}