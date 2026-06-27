package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.JobRequest;
import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Job;

import java.util.List;

public interface JobService {
    Job create(JobRequest request);
    Job update(Long id, JobRequest request);
    void delete(Long id);
    Job getById(Long id);
    List<Job> getAll(String query, JobStatus status);
    List<Job> getMine();
}