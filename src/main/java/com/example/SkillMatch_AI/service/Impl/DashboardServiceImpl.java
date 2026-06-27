package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.response.DashboardSummaryResponse;
import com.example.SkillMatch_AI.repository.ApplicationRepository;
import com.example.SkillMatch_AI.repository.CompanyRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public DashboardSummaryResponse summary() {
        return new DashboardSummaryResponse(
                userRepository.count(),
                companyRepository.count(),
                jobRepository.count(),
                applicationRepository.count()
        );
    }
}