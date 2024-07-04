package com.example.anonyboard.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    @Value("${jwt.secret}")
    private String key;
    private SecretKey secretKey;
    private static final Long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30L;
    private static final Long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L * 24 * 7;

    @PostConstruct
    private void setSecretKey(){
        secretKey = Keys.hmacShaKeyFor(key.getBytes());
    }

    public String generateAccessToken(String username, String email, String nickname, String role){
        String access_token = generateToken(username, email, nickname, role, ACCESS_TOKEN_EXPIRE_TIME);
        return access_token;
    }

    public String generateRefreshToken(String username, String email, String nickname, String role){
        String refresh_token = generateToken(username, email, nickname, role, REFRESH_TOKEN_EXPIRE_TIME);
        return refresh_token;
    }

    private String generateToken(String username, String email, String nickname, String role, Long expireTime){
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expireTime);

        return Jwts.builder()
                .claim("username", username)
                .claim("nickname", nickname)
                .claim("email", email)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public String getUsername(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (String) claims.get("username");
    }
    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (String) claims.get("email");
    }
    public String getNickname(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (String) claims.get("nickname");
    }
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return (String) claims.get("role");
    }
    public boolean isExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            return true;
        }
    }
}
