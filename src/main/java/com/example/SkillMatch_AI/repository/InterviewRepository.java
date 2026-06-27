package com.example.SkillMatch_AI.repository;

import com.example.SkillMatch_AI.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByCandidateId(Long candidateId);
    List<Interview> findByInterviewerId(Long interviewerId);
    List<Interview> findByJobId(Long jobId);
}