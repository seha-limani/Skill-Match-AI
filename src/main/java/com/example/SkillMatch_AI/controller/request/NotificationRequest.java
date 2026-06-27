package com.example.SkillMatch_AI.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificationRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String message;
    @NotNull
    private Long recipientId;
}