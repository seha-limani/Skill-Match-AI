package com.example.SkillMatch_AI.controller.response;

public record AIAnalysisResponse(
        Long id,
        Double resumeScore,
        String missingSkills,
        String recommendations,
        String analyzedAt,
        Long resumeId,
        Long jobId
) {
}