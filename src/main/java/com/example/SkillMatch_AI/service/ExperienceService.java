package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.ExperienceRequest;
import com.example.SkillMatch_AI.model.Experience;

import java.util.List;

public interface ExperienceService {
    Experience create(ExperienceRequest request);
    Experience update(Long id, ExperienceRequest request);
    void delete(Long id);
    List<Experience> mine();
}