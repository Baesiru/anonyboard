package com.example.anonyboard.controller;

import com.example.anonyboard.config.CustomUserDetails;
import com.example.anonyboard.dto.UserDto;
import com.example.anonyboard.entity.User;
import com.example.anonyboard.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/api/register")
    public ResponseEntity<User> register(@RequestBody UserDto userDto){
        User newUser = userService.createUser(userDto);
        if (newUser == null){
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.status(201).body(newUser);
    }
    @GetMapping("/api/check")
    public ResponseEntity<Object> check(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        return ResponseEntity.status(201).body("아이디 : " + userDetails.getUsername() + "\n권한 : " + userDetails.getAuthorities().toArray()[0]);
    }
    @GetMapping("/api/check/username")
    public ResponseEntity<Object> checkUsername(@NotBlank(message="아이디 값을 입력해야 합니다.")
                                                    @Size(min=4, max=12, message="아이디는 4글자 이상 12글자 이하이여야 합니다.")
                                                    @RequestParam String username){
        User checkUser = userService.checkUsername(username);
        if (checkUser != null){
            return ResponseEntity.status(400).body("이미 존재하는 아이디입니다");
        }
        return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
    }
}
