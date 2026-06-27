package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.EducationRequest;
import com.example.SkillMatch_AI.model.Education;
import com.example.SkillMatch_AI.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Education create(@Valid @RequestBody EducationRequest request) {
        return educationService.create(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public List<Education> mine() {
        return educationService.mine();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public Education update(@PathVariable Long id, @Valid @RequestBody EducationRequest request) {
        return educationService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'ADMIN')")
    public void delete(@PathVariable Long id) {
        educationService.delete(id);
    }
}