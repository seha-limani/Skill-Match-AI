package com.example.SkillMatch_AI.controller.request;

import com.example.SkillMatch_AI.model.Enum.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApplicationRequest {
    @NotNull(message = "Job id is required")
    private Long jobId;

    private Long resumeId;

    private ApplicationStatus status;
    private Double matchScore;
    private String aiSummary;
}