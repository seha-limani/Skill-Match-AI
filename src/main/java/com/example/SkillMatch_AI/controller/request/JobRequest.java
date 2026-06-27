package com.example.SkillMatch_AI.controller.request;

import com.example.SkillMatch_AI.model.Enum.ExperienceLevel;
import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Enum.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record JobRequest(
        @NotBlank(message = "Job title is required")
        @Size(min = 2, max = 100, message = "Job title must be between 2 and 100 characters")
        String title,

        @NotBlank(message = "Job description is required")
        @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
        String description,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than 0")
        Double salary,

        String location,

        String requiredSkills,

        @NotNull(message = "Job type is required")
        JobType jobType,

        JobStatus status,

        ExperienceLevel experienceLevel,

        Long companyId
) {}