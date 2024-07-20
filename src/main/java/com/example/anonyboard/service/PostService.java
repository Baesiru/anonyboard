package com.example.anonyboard.service;

import com.example.anonyboard.dto.PostDto;
import com.example.anonyboard.entity.Post;
import com.example.anonyboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Post createPost(PostDto postDto, boolean isUser) {
        if (isUser == false)
            postDto.setPassword(passwordEncoder.encode(postDto.getPassword()));
        Post post = postDto.toEntity(isUser);
        postRepository.save(post);
        return post;
    }

    public Post showPost(Long id){
        Post post = postRepository.findById(id).orElse(null);
        return post;
    }
}
