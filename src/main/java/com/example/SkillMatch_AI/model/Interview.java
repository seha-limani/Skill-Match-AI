package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.InterviewStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interviewes")
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime interviewDate;

    private String meetingLink;

    @Enumerated(EnumType.STRING)
    private InterviewStatus status;

    private String notes;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
    private Application application;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user", "company"})
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
    private User interviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
    private User candidate;

    @PrePersist
    void onCreate() {
        if (status == null) {
            status = InterviewStatus.SCHEDULED;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
