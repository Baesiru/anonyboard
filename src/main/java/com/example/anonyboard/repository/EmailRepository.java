package com.example.anonyboard.repository;

import com.example.anonyboard.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, String> {
    public Optional<Email> findByEmail(String email);
}
