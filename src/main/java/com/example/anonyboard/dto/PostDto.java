package com.example.anonyboard.dto;

import com.example.anonyboard.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String username;
    private String password;
    private LocalDateTime localDateTime;
    public Post toEntity(boolean isUser){
        localDateTime = LocalDateTime.now();
        return new Post(id, title, content, nickname,username, password, localDateTime, isUser, 0);
    }
}
