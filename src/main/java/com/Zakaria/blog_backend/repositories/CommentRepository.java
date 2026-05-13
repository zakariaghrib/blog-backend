package com.Zakaria.blog_backend.repositories;

import com.Zakaria.blog_backend.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Cette ligne magique permettra de trouver tous les commentaires d'un article spécifique !
    List<Comment> findByPostId(Long postId);
}