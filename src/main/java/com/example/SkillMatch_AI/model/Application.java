package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.ApplicationStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Double matchScore;

    private LocalDateTime appliedAt;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences"})
    private User applicant;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties({"user", "company"})
    private Job job;

    @ManyToOne
    @JsonIgnoreProperties({"user"})
    private Resume resume;

    @Column(length = 1000)
    private String aiSummary;

    @PrePersist
    void onCreate() {
        if (status == null) {
            status = ApplicationStatus.APPLIED;
        }
        if (appliedAt == null) {
            appliedAt = LocalDateTime.now();
        }
    }
}
