package com.example.anonyboard.config.security;

import com.example.anonyboard.config.security.exception.CustomAuthenticationFailureHandler;
import com.example.anonyboard.config.security.filter.JWTFilter;
import com.example.anonyboard.config.security.filter.LoginFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;
    private final CustomAuthenticationFailureHandler failureHandler;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, TokenProvider tokenProvider, ObjectMapper objectMapper, CustomAuthenticationFailureHandler failureHandler) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
        this.failureHandler = failureHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain defaulSecurityFilterChain(HttpSecurity http) throws Exception{

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll());

        http.
                addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), tokenProvider, objectMapper, failureHandler), UsernamePasswordAuthenticationFilter.class);
        http.
                addFilterBefore(new JWTFilter(tokenProvider),LoginFilter.class);

                return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFailureHandler failureHandler(){
        return new CustomAuthenticationFailureHandler();
    }
}
