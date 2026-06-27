package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.model.SavedJob;
import com.example.SkillMatch_AI.service.SavedJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saved-jobs")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public SavedJob save(@PathVariable Long jobId) {
        return savedJobService.save(jobId);
    }

    @DeleteMapping("/{jobId}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public void remove(@PathVariable Long jobId) {
        savedJobService.remove(jobId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<SavedJob> mine() {
        return savedJobService.getMine();
    }
}