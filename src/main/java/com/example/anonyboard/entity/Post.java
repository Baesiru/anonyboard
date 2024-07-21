package com.example.anonyboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String nickname;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private LocalDateTime currentDateTime;
    @Column
    private boolean isUser;
    @Column
    private int views;
}
