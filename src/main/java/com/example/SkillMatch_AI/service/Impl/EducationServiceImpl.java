package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.EducationRequest;
import com.example.SkillMatch_AI.model.Education;
import com.example.SkillMatch_AI.repository.EducationRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Education create(EducationRequest request) {
        Education education = new Education();
        apply(education, request);
        education.setUser(currentUserService.get());
        return educationRepository.save(education);
    }

    @Override
    @Transactional
    public Education update(Long id, EducationRequest request) {
        Education education = educationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Education not found"));
        apply(education, request);
        return educationRepository.save(education);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        educationRepository.deleteById(id);
    }

    @Override
    public List<Education> mine() {
        return educationRepository.findByUserId(currentUserService.get().getId());
    }

    private void apply(Education education, EducationRequest request) {
        education.setInstitution(request.getInstitution());
        education.setDegree(request.getDegree());
        education.setFieldOfStudy(request.getFieldOfStudy());
        education.setStartDate(request.getStartDate());
        education.setEndDate(request.getEndDate());
        education.setCurrent(request.getCurrent());
    }
}