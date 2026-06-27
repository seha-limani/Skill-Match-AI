package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.ResumeRequest;
import com.example.SkillMatch_AI.model.Resume;
import com.example.SkillMatch_AI.model.User;
import com.example.SkillMatch_AI.repository.ResumeRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Resume create(ResumeRequest request) {
        User currentUser = currentUserService.get();
        Resume resume = new Resume();
        resume.setUser(currentUser);
        resume.setFileName(request.getFileName());
        resume.setFileUrl(request.getFileUrl());
        resume.setFileType(request.getFileType());
        resume.setExtractedText(request.getExtractedText());
        return resumeRepository.save(resume);
    }

    @Override
    public Resume getById(Long id) {
        return resumeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Resume not found"));
    }

    @Override
    public List<Resume> getMine() {
        return resumeRepository.findByUserId(currentUserService.get().getId());
    }
}