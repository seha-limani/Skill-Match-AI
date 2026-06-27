package com.example.SkillMatch_AI.controller.response;

import com.example.SkillMatch_AI.model.Enum.Role;
import com.example.SkillMatch_AI.model.User;

public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String profileImage,
        String bio,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfileImage(),
                user.getBio(),
                user.getRole()
        );
    }
}