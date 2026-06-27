package com.example.SkillMatch_AI.service;

import com.example.SkillMatch_AI.model.SavedJob;

import java.util.List;

public interface SavedJobService {
    SavedJob save(Long jobId);
    void remove(Long jobId);
    List<SavedJob> getMine();
}