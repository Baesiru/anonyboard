package com.example.anonyboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
    @Column
    private String name;
    @Column(unique = true)
    private String nickname;
    @Column
    private String email;
    @Column
    private String role;
    @Column
    private String created_date;
    @Column
    private String modified_date;

}
