package com.example.SkillMatch_AI.controller.response;

public record AuthResponse(String tokenType, String accessToken, UserResponse user) {
    public static AuthResponse bearer(String accessToken, UserResponse user) {
        return new AuthResponse("Bearer", accessToken, user);
    }
}