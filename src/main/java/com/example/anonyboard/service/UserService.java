package com.example.anonyboard.service;

import com.example.anonyboard.dto.UserDto;
import com.example.anonyboard.entity.User;
import com.example.anonyboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(UserDto userDto) {
        Boolean isExist = userRepository.existsByUsername(userDto.getUsername());
        if(isExist){
            return null;
        }

        String hashedPassword = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(hashedPassword);
        User user = userDto.toEntity();
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User checkUsername(String username) {
        User checkUser = userRepository.findByUsername(username);
        return checkUser;
    }
}
