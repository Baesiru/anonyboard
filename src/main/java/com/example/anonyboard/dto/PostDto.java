package com.example.anonyboard.dto;

import com.example.anonyboard.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String password;
    public Post toEntity(boolean isUser){
        return new Post(id, title, content, nickname, password, isUser);
    }
}
