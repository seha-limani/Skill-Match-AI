package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.ExperienceRequest;
import com.example.SkillMatch_AI.model.Experience;
import com.example.SkillMatch_AI.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Experience create(@Valid @RequestBody ExperienceRequest request) {
        return experienceService.create(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<Experience> mine() {
        return experienceService.mine();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Experience update(@PathVariable Long id, @Valid @RequestBody ExperienceRequest request) {
        return experienceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public void delete(@PathVariable Long id) {
        experienceService.delete(id);
    }
}