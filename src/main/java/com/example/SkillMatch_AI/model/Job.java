package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.ExperienceLevel;
import com.example.SkillMatch_AI.model.Enum.JobStatus;
import com.example.SkillMatch_AI.model.Enum.JobType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Job title is required")
    @Size(min = 2, max = 100, message = "Job title must be between 2 and 100 characters")
    @Column(nullable = false)
    private String title;


    @NotBlank(message = "Job description is required")
    @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
    @Column(nullable = false, length = 2000)
    private String description;


    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be greater than 0")
    private Double salary;

    private String location;

    @Column(length = 1000)
    private String requiredSkills;

    @NotNull(message = "Job type is required")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @NotNull(message = "Job status is required")
    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;

    @ManyToOne
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences"})
    private User user;

    @ManyToOne
    @JsonIgnoreProperties({"user", "jobs"})
    private Company company;

    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (status == null) {
            status = JobStatus.OPEN;
        }
        createdAt = LocalDateTime.now();
    }
}
