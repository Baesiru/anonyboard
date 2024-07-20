package com.example.anonyboard.controller;

import com.example.anonyboard.config.security.CustomUserDetails;
import com.example.anonyboard.dto.PostDto;
import com.example.anonyboard.entity.Post;
import com.example.anonyboard.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Object> createPost(@RequestBody PostDto postDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
            postDto.setNickname(user.getNickname());
            Post post = postService.createPost(postDto, true);
            return ResponseEntity.ok(post);
        } catch (ClassCastException e){ //비로그인 유저가 게시글 작성시
            Post post = postService.createPost(postDto, false);
            return ResponseEntity.ok().body(post);
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Object> showPost(@PathVariable Long id) {
        Post post = postService.showPost(id);
        return ResponseEntity.ok().body(post);
    }
}
