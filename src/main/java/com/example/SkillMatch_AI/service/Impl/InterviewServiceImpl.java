package com.example.SkillMatch_AI.service.Impl;

import com.example.SkillMatch_AI.controller.request.InterviewRequest;
import com.example.SkillMatch_AI.model.Application;
import com.example.SkillMatch_AI.model.Interview;
import com.example.SkillMatch_AI.repository.ApplicationRepository;
import com.example.SkillMatch_AI.repository.InterviewRepository;
import com.example.SkillMatch_AI.repository.JobRepository;
import com.example.SkillMatch_AI.repository.UserRepository;
import com.example.SkillMatch_AI.service.CurrentUserService;
import com.example.SkillMatch_AI.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    @Override
    @Transactional
    public Interview schedule(InterviewRequest request) {
        Application application = applicationRepository.findById(request.getApplicationId()).orElseThrow(() -> new IllegalArgumentException("Application not found"));
        Interview interview = new Interview();
        interview.setApplication(application);
        interview.setCandidate(application.getApplicant());
        interview.setJob(request.getJobId() == null ? application.getJob() : jobRepository.findById(request.getJobId()).orElseThrow(() -> new IllegalArgumentException("Job not found")));
        interview.setInterviewer(request.getInterviewerId() == null ? currentUserService.get() : userRepository.findById(request.getInterviewerId()).orElseThrow(() -> new IllegalArgumentException("Interviewer not found")));
        interview.setInterviewDate(request.getInterviewDate());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setStatus(request.getStatus());
        interview.setNotes(request.getNotes());
        return interviewRepository.save(interview);
    }

    @Override
    @Transactional
    public Interview update(Long id, InterviewRequest request) {
        Interview interview = getById(id);
        interview.setInterviewDate(request.getInterviewDate());
        interview.setMeetingLink(request.getMeetingLink());
        interview.setStatus(request.getStatus());
        interview.setNotes(request.getNotes());
        return interviewRepository.save(interview);
    }

    @Override
    public Interview getById(Long id) {
        return interviewRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Interview not found"));
    }

    @Override
    public List<Interview> getMine() {
        var currentUser = currentUserService.get();
        return interviewRepository.findByCandidateId(currentUser.getId());
    }
}