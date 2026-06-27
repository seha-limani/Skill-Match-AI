package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.JobRequest;
import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Job;
import com.example.SkillMatch_AI.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @GetMapping
    public List<Job> all(@RequestParam(required = false) String q, @RequestParam(required = false) JobStatus status) {
        return jobService.getAll(q, status);
    }

    @GetMapping("/{id}")
    public Job byId(@PathVariable Long id) {
        return jobService.getById(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public List<Job> mine() {
        return jobService.getMine();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Job create(@Valid @RequestBody JobRequest request) {
        return jobService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Job update(@PathVariable Long id, @Valid @RequestBody JobRequest request) {
        return jobService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public void delete(@PathVariable Long id) {
        jobService.delete(id);
    }
}