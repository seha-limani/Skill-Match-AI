package com.example.SkillMatch_AI.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResumeRequest {
    @NotBlank(message = "File name is required")
    private String fileName;

    private String fileUrl;

    @NotBlank(message = "File type is required")
    private String fileType;

    @NotBlank(message = "Extracted text is required")
    private String extractedText;
}