package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.ExperienceRequest;
import com.example.SkillMatch_AI.model.Experience;
import com.example.SkillMatch_AI.repository.ExperienceRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Experience create(ExperienceRequest request) {
        Experience experience = new Experience();
        apply(experience, request);
        experience.setUser(currentUserService.get());
        return experienceRepository.save(experience);
    }

    @Override
    @Transactional
    public Experience update(Long id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Experience not found"));
        apply(experience, request);
        return experienceRepository.save(experience);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        experienceRepository.deleteById(id);
    }

    @Override
    public List<Experience> mine() {
        return experienceRepository.findByUserId(currentUserService.get().getId());
    }

    private void apply(Experience experience, ExperienceRequest request) {
        experience.setCompanyName(request.getCompanyName());
        experience.setPosition(request.getPosition());
        experience.setDescription(request.getDescription());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setCurrent(request.getCurrent());
    }
}