package com.example.anonyboard.config.security.filter;

import com.example.anonyboard.config.security.CustomUserDetails;
import com.example.anonyboard.config.security.TokenProvider;
import com.example.anonyboard.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        String token = null;
        if (authorization == null) {
            token = regenerateAccessToken(request, response);
            if (token == null){
                filterChain.doFilter(request,response);
                return;
            }
        } else {
            token = authorization.substring("Bearer ".length());
            if (tokenProvider.isExpired(token)) {
                token = regenerateAccessToken(request, response);
                if (token == null) {
                    filterChain.doFilter(request, response);
                    return;
                }
            }
            else{
                token = authorization.substring("Bearer ".length());
            }
        }

        String username = tokenProvider.getUsername(token);
        String email = tokenProvider.getEmail(token);
        String nickname = tokenProvider.getNickname(token);
        String role = tokenProvider.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setPassword("temp");
        user.setEmail(email);
        user.setNickname(nickname);
        user.setRole(role);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }

    public String regenerateAccessToken(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String refresh = null;
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refreshToken")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }
        if (refresh != null) {
            String username2 = tokenProvider.getUsername(refresh);
            String email2 = tokenProvider.getEmail(refresh);
            String nickname2 = tokenProvider.getNickname(refresh);
            String role2 = tokenProvider.getRole(refresh);
            token = tokenProvider.generateAccessToken(username2, email2, nickname2, role2);
            String newRefreshToken = tokenProvider.generateRefreshToken(username2, email2, nickname2, role2);
            response.addHeader("Authorization", "Bearer " + token);
            ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                    .maxAge(7 * 24 * 60 * 60)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .httpOnly(true)
                    .build();
            response.setHeader("Set-Cookie", cookie.toString());
            return token;
        } else {
            return null;
        }
    }
}
