package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.EducationRequest;
import com.example.SkillMatch_AI.model.Education;

import java.util.List;

public interface EducationService {
    Education create(EducationRequest request);
    Education update(Long id, EducationRequest request);
    void delete(Long id);
    List<Education> mine();
}