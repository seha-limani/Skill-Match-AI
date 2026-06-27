package com.example.SkillMatch_AI.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EducationRequest {
    @NotBlank
    private String institution;
    @NotBlank
    private String degree;
    private String fieldOfStudy;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean current;
}