package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.config.JwtService;
import com.example.SkillMatch_AI.controller.request.LoginRequest;
import com.example.SkillMatch_AI.controller.request.UserRequest;
import com.example.SkillMatch_AI.controller.response.AuthResponse;
import com.example.SkillMatch_AI.controller.response.UserResponse;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CurrentUserService currentUserService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody UserRequest request) {
        User user = userService.addUser(request);
        return AuthResponse.bearer(jwtService.generateToken(user), UserResponse.from(user));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail().trim().toLowerCase(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return AuthResponse.bearer(jwtService.generateToken(user), UserResponse.from(user));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh() {
        User user = currentUserService.get();
        return AuthResponse.bearer(jwtService.generateToken(user), UserResponse.from(user));
    }
}