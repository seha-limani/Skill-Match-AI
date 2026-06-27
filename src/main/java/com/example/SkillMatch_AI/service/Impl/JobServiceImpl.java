package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.JobRequest;
import com.example.SkillMatch_AI.model.Company;
import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Enum.Role;
import com.example.SkillMatch_AI.model.Job;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.repository.CompanyRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Job create(JobRequest request) {
        User currentUser = currentUserService.get();
        Job job = new Job();
        applyRequest(job, request);
        job.setUser(currentUser);
        if (request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
            job.setCompany(company);
        }
        return jobRepository.save(job);
    }

    @Override
    @Transactional
    public Job update(Long id, JobRequest request) {
        Job existing = getById(id);
        ensureOwnerOrAdmin(existing);
        applyRequest(existing, request);
        if (request.companyId() != null) {
            Company company = companyRepository.findById(request.companyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found"));
            existing.setCompany(company);
        }
        return jobRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Job existing = getById(id);
        ensureOwnerOrAdmin(existing);
        jobRepository.delete(existing);
    }

    @Override
    public Job getById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
    }

    @Override
    public List<Job> getAll(String query, JobStatus status) {
        List<Job> jobs = query == null || query.isBlank()
                ? jobRepository.findAll()
                : jobRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(query, query, query);
        if (status != null) {
            jobs = jobs.stream().filter(job -> status.equals(job.getStatus())).toList();
        }
        return jobs;
    }

    @Override
    public List<Job> getMine() {
        User currentUser = currentUserService.get();
        return jobRepository.findByUserId(currentUser.getId());
    }

    private void ensureOwnerOrAdmin(Job job) {
        User currentUser = currentUserService.get();
        if (!currentUser.getId().equals(job.getUser().getId()) && currentUser.getRole() != Role.ADMIN) {
            throw new IllegalArgumentException("Not allowed");
        }
    }

    private void applyRequest(Job job, JobRequest request) {
        job.setTitle(request.title());
        job.setDescription(request.description());
        job.setSalary(request.salary());
        job.setLocation(request.location());
        job.setRequiredSkills(request.requiredSkills());
        job.setJobType(request.jobType());
        job.setStatus(request.status() == null ? JobStatus.OPEN : request.status());
        job.setExperienceLevel(request.experienceLevel());
    }
}