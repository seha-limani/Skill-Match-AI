package com.example.SkillMatch_AI.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExperienceRequest {
    @NotBlank
    private String companyName;
    @NotBlank
    private String position;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean current;
}