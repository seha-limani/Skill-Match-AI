package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.response.AIAnalysisResponse;
import com.example.SkillMatch_AI.model.AIAnalysis;

import java.util.List;

public interface AIAnalysisService {
    AIAnalysis analyze(Long resumeId, Long jobId);
    List<AIAnalysis> mine();
    AIAnalysisResponse toResponse(AIAnalysis analysis);
}