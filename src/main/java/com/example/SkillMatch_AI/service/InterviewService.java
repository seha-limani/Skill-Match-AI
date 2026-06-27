package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.InterviewRequest;
import com.example.SkillMatch_AI.model.Interview;

import java.util.List;

public interface InterviewService {
    Interview schedule(InterviewRequest request);
    Interview update(Long id, InterviewRequest request);
    Interview getById(Long id);
    List<Interview> getMine();
}