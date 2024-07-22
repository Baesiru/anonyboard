package com.example.anonyboard2.config.security.filter;

import com.example.anonyboard2.config.security.CustomUserDetails;
import com.example.anonyboard2.config.security.TokenProvider;
import com.example.anonyboard2.config.security.exception.IllegalPasswordException;
import com.example.anonyboard2.config.security.exception.IllegalUsernameException;
import com.example.anonyboard2.dto.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    public LoginFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, ObjectMapper objectMapper){
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();

        if (username == null)
            throw new IllegalUsernameException("아이디를 입력해주세요");
        if (password == null)
            throw new IllegalPasswordException("비밀번호를 입력해주세요");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException{
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        String email = customUserDetails.getEmail();
        String nickname = customUserDetails.getNickname();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();
        String access_token = tokenProvider.generateAccessToken(username, email, nickname, role);
        String refresh_token = tokenProvider.generateRefreshToken(username, email, nickname, role);
        response.addHeader("Authorization", "Bearer " + access_token);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refresh_token)
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        response.getWriter().write(username+"님 환영합니다.");
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        throw new BadCredentialsException("아이디 또는 비밀번호를 확인해주세요");
    }

}
