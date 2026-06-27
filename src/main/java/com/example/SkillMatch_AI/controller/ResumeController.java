package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.ResumeRequest;
import com.example.SkillMatch_AI.model.Resume;
import com.example.SkillMatch_AI.service.ResumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Resume create(@Valid @RequestBody ResumeRequest request) {
        return resumeService.create(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<Resume> mine() {
        return resumeService.getMine();
    }

    @GetMapping("/{id}")
    public Resume byId(@PathVariable Long id) {
        return resumeService.getById(id);
    }
}