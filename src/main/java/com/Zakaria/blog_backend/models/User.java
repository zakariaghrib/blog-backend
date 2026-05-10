package com.Zakaria.blog_backend.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Post> posts;
}