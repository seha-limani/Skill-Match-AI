package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.UserRequest;
import com.example.SkillMatch_AI.controller.response.UserResponse;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CurrentUserService currentUserService;

    @GetMapping("/me")
    public UserResponse me() {
        return UserResponse.from(currentUserService.get());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> all() {
        return userService.getAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse byId(@PathVariable Long id) {
        return UserResponse.from(userService.getById(id));
    }

    @PutMapping("/me")
    public UserResponse updateMe(@Valid @RequestBody UserRequest request) {
        return UserResponse.from(userService.updateCurrentUser(request));
    }
}
