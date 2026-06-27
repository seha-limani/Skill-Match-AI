package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.ApplicationRequest;
import com.example.SkillMatch_AI.model.Application;
import com.example.SkillMatch_AI.model.Job;
import com.example.SkillMatch_AI.model.Resume;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.repository.ApplicationRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.repository.ResumeRepository;
import com.example.SkillMatch_AI.service.ApplicationService;
import com.example.SkillMatch_AI.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Application apply(ApplicationRequest request) {
        User currentUser = currentUserService.get();
        Job job = jobRepository.findById(request.getJobId()).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        Application application = new Application();
        application.setApplicant(currentUser);
        application.setJob(job);
        if (request.getResumeId() != null) {
            Resume resume = resumeRepository.findById(request.getResumeId()).orElseThrow(() -> new IllegalArgumentException("Resume not found"));
            application.setResume(resume);
        }
        application.setMatchScore(request.getMatchScore());
        application.setAiSummary(request.getAiSummary());
        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }
        return applicationRepository.save(application);
    }

    @Override
    @Transactional
    public Application update(Long id, ApplicationRequest request) {
        Application existing = getById(id);
        if (request.getStatus() != null) {
            existing.setStatus(request.getStatus());
        }
        existing.setMatchScore(request.getMatchScore());
        existing.setAiSummary(request.getAiSummary());
        return applicationRepository.save(existing);
    }

    @Override
    public Application getById(Long id) {
        return applicationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Application not found"));
    }

    @Override
    public List<Application> getMine() {
        return applicationRepository.findByApplicantId(currentUserService.get().getId());
    }

    @Override
    public List<Application> getForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}