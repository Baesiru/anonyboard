package com.example.anonyboard.service;

import com.example.anonyboard.config.security.CustomUserDetails;
import com.example.anonyboard.dto.PostDto;
import com.example.anonyboard.entity.Post;
import com.example.anonyboard.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Post createPost(PostDto postDto, boolean isUser) {
        if (isUser == false){
            if (postDto.getNickname() == null){
                throw new IllegalArgumentException("닉네임을 입력해야 합니다.");
            }
            if (postDto.getPassword() == null){
                throw new IllegalArgumentException("비밀번호를 입력해야 합니다.");
            }
            postDto.setPassword(passwordEncoder.encode(postDto.getPassword()));
        }
        Post post = postDto.toEntity(isUser);
        postRepository.save(post);
        return post;
    }

    @Transactional
    public Post showPost(Long id){
        Post post = postRepository.findById(id).orElse(null);
        if (post!=null){
            post.setViews(post.getViews()+1);
            postRepository.save(post);
        }
        else {
            throw new NoSuchElementException("존재하지 않는 게시글입니다");
        }
        return post;
    }


    public Page<Post> showPosts(int page) {
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "id")));
        return posts;
    }

    public void deletePost(Long id, String password) {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            throw new NoSuchElementException("존재하지 않는 게시글입니다");
        }
        if (post.isUser()){
            try{
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
                String username = user.getUsername();
                if (post.getUsername().equals(username)) {
                    postRepository.deleteById(id);
                }
                else {
                    throw new IllegalArgumentException("게시글 작성자가 아닙니다");
                }

            } catch (ClassCastException e){ //비로그인 유저가 게시글 작성시
                throw new IllegalArgumentException("회원이 작성한 글입니다");
            }
        }

        else {
            if (passwordEncoder.matches(password, post.getPassword())){
                postRepository.deleteById(id);
            }
            else{
                throw new IllegalArgumentException("비밀번호가 틀립니다");
            }
        }
    }
}
