package com.example.SkillMatch_AI.controller;

import com.example.SkillMatch_AI.controller.response.DashboardSummaryResponse;
import com.example.SkillMatch_AI.controller.response.UserResponse;
import com.example.SkillMatch_AI.repository.CompanyRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.service.DashboardService;
import com.example.SkillMatch_AI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final DashboardService dashboardService;
    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardSummaryResponse summary() {
        return dashboardService.summary();
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> users() {
        return userService.getAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/companies")
    @PreAuthorize("hasRole('ADMIN')")
    public Object companies() {
        return companyRepository.findAll();
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('ADMIN')")
    public Object jobs() {
        return jobRepository.findAll();
    }
}