package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.request.InterviewRequest;
import com.example.SkillMatch_AI.model.Interview;
import com.example.SkillMatch_AI.service.InterviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Interview schedule(@Valid @RequestBody InterviewRequest request) {
        return interviewService.schedule(request);
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('JOB_SEEKER', 'EMPLOYER', 'ADMIN')")
    public List<Interview> mine() {
        return interviewService.getMine();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYER', 'ADMIN')")
    public Interview update(@PathVariable Long id, @Valid @RequestBody InterviewRequest request) {
        return interviewService.update(id, request);
    }
}