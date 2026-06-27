package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.ApplicationRequest;
import com.example.SkillMatch_AI.model.Application;
import com.example.SkillMatch_AI.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Application apply(@Valid @RequestBody ApplicationRequest request) {
        return applicationService.apply(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<Application> mine() {
        return applicationService.getMine();
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public List<Application> forJob(@PathVariable Long jobId) {
        return applicationService.getForJob(jobId);
    }

    @GetMapping("/{id}")
    public Application byId(@PathVariable Long id) {
        return applicationService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Application update(@PathVariable Long id, @RequestBody ApplicationRequest request) {
        return applicationService.update(id, request);
    }
}