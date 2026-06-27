package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.response.AIAnalysisResponse;
import com.example.SkillMatch_AI.model.AIAnalysis;
import com.example.SkillMatch_AI.service.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai-analysis")
@RequiredArgsConstructor
public class AIAnalysisController {

    private final AIAnalysisService aiAnalysisService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'EMPLOYER', 'ADMIN')")
    public AIAnalysisResponse analyze(@RequestParam Long resumeId, @RequestParam Long jobId) {
        AIAnalysis analysis = aiAnalysisService.analyze(resumeId, jobId);
        return aiAnalysisService.toResponse(analysis);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<AIAnalysisResponse> mine() {
        return aiAnalysisService.mine().stream().map(aiAnalysisService::toResponse).toList();
    }
}