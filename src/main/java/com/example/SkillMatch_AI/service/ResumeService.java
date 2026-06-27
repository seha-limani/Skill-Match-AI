package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.controller.request.ResumeRequest;
import com.example.SkillMatch_AI.model.Resume;

import java.util.List;

public interface ResumeService {
    Resume create(ResumeRequest request);
    Resume getById(Long id);
    List<Resume> getMine();
}