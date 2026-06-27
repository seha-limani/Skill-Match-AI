package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.response.AIAnalysisResponse;
import com.example.SkillMatch_AI.model.AIAnalysis;
import com.example.SkillMatch_AI.model.Job;
import com.example.SkillMatch_AI.model.Resume;
import com.example.SkillMatch_AI.repository.AIAnalysisRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.repository.ResumeRepository;
import com.example.SkillMatch_AI.service.AIAnalysisService;
import com.example.SkillMatch_AI.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AIAnalysisServiceImpl implements AIAnalysisService {

    private final AIAnalysisRepository aiAnalysisRepository;
    private final ResumeRepository resumeRepository;
    private final JobRepository jobRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public AIAnalysis analyze(Long resumeId, Long jobId) {
        Resume resume = resumeRepository.findById(resumeId).orElseThrow(() -> new IllegalArgumentException("Resume not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));

        AIAnalysis analysis = new AIAnalysis();
        analysis.setResume(resume);
        analysis.setJob(job);
        analysis.setResumeScore(80.0);
        analysis.setMissingSkills("Docker, AWS, Kubernetes");
        analysis.setRecommendations("Add cloud and container skills, then apply again.");
        return aiAnalysisRepository.save(analysis);
    }

    @Override
    public List<AIAnalysis> mine() {
        return aiAnalysisRepository.findAll().stream()
                .filter(item -> item.getResume() != null && item.getResume().getUser() != null && item.getResume().getUser().getId().equals(currentUserService.get().getId()))
                .toList();
    }

    @Override
    public AIAnalysisResponse toResponse(AIAnalysis analysis) {
        return new AIAnalysisResponse(
                analysis.getId(),
                analysis.getResumeScore(),
                analysis.getMissingSkills(),
                analysis.getRecommendations(),
                analysis.getAnalyzedAt() == null ? null : analysis.getAnalyzedAt().toString(),
                analysis.getResume() == null ? null : analysis.getResume().getId(),
                analysis.getJob() == null ? null : analysis.getJob().getId()
        );
    }
}