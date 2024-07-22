package com.example.anonyboard2.repository;

import com.example.anonyboard2.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}