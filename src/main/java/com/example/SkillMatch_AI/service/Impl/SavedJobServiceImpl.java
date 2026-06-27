package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.model.Job;
import com.example.SkillMatch_AI.model.SavedJob;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.repository.SavedJobRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public SavedJob save(Long jobId) {
        var user = currentUserService.get();
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        return savedJobRepository.findByUserIdAndJobId(user.getId(), jobId).orElseGet(() -> {
            SavedJob savedJob = new SavedJob();
            savedJob.setUser(user);
            savedJob.setJob(job);
            return savedJobRepository.save(savedJob);
        });
    }

    @Override
    @Transactional
    public void remove(Long jobId) {
        var user = currentUserService.get();
        savedJobRepository.findByUserIdAndJobId(user.getId(), jobId).ifPresent(savedJobRepository::delete);
    }

    @Override
    public List<SavedJob> getMine() {
        return savedJobRepository.findByUserId(currentUserService.get().getId());
    }
}