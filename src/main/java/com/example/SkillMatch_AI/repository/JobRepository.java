package com.example.SkillMatch_AI.repository;

import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByStatus(JobStatus status);
    List<Job> findByUserId(Long userId);
    List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrRequiredSkillsContainingIgnoreCase(
            String title,
            String description,
            String requiredSkills
    );
}
