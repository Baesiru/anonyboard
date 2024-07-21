package com.example.anonyboard.controller;

import com.example.anonyboard.config.security.CustomUserDetails;
import com.example.anonyboard.dto.PostDto;
import com.example.anonyboard.entity.Post;
import com.example.anonyboard.service.PostService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDto postDto){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
            postDto.setNickname(user.getNickname());
            postDto.setUsername(user.getUsername());
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

    @GetMapping("/post")
    public ResponseEntity<Object> showPosts(@RequestParam int page) {
        List<Post> posts = postService.showPosts(page).getContent();
        return ResponseEntity.ok().body(posts);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id, @RequestParam(required = false) String password) {
        postService.deletePost(id, password);
        return ResponseEntity.ok().body("해당 게시글이 정상적으로 삭제되었습니다.");
    }
}
