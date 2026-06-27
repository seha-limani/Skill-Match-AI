package com.example.SkillMatch_AI.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank
    private String content;
    @NotNull
    private Long receiverId;
}