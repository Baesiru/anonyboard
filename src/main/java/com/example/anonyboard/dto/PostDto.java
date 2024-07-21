package com.example.anonyboard.dto;

import com.example.anonyboard.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    @NotBlank(message="제목을 입력해야 합니다.")
    private String title;
    @NotBlank(message="내용을 입력해야 합니다.")
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
