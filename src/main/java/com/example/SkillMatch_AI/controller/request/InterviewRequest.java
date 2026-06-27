package com.example.SkillMatch_AI.controller.request;

import com.example.SkillMatch_AI.model.Enum.InterviewStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewRequest {
    @NotNull
    private Long applicationId;
    private Long jobId;
    private LocalDateTime interviewDate;
    private String meetingLink;
    private InterviewStatus status;
    private String notes;
    private Long interviewerId;
    private Long candidateId;
}