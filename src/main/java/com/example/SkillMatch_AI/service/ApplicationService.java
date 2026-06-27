package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.ApplicationRequest;
import com.example.SkillMatch_AI.model.Application;

import java.util.List;

public interface ApplicationService {
    Application apply(ApplicationRequest request);
    Application update(Long id, ApplicationRequest request);
    Application getById(Long id);
    List<Application> getMine();
    List<Application> getForJob(Long jobId);
}