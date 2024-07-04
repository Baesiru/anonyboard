package com.example.anonyboard.controller;

import com.example.anonyboard.dto.UserDto;
import com.example.anonyboard.entity.User;
import com.example.anonyboard.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto){
        User newUser = userService.createUser(userDto);
        if (newUser == null){
            return ResponseEntity.status(400).body("이미 존재하는 계정입니다.");
        }
        return ResponseEntity.status(201).body(newUser);
    }

    @GetMapping("/check")
    public ResponseEntity<Object> check(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.status(201).body("아이디 : " + authentication.getName() + ", 권한 : " + roles);
    }
    @GetMapping("/check/username")
    public ResponseEntity<Object> checkUsername(@NotBlank(message="아이디 값을 입력해야 합니다.")
                                                    @Size(min=4, max=12, message="아이디는 4글자 이상 12글자 이하이여야 합니다.")
                                                    @RequestParam String username,  @Size(min=4, max=12, message="비밀번호는 4글자 이상 12글자 이하이여야 합니다.") @RequestParam String password){
        User checkUser = userService.checkUsername(username);
        if (checkUser != null){
            return ResponseEntity.status(400).body("이미 존재하는 아이디입니다");
        }
        return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
    }
}
