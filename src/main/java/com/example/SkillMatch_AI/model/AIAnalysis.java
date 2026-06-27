package com.example.SkillMatch_AI.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ai_analysis")
public class AIAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double resumeScore;

    private String missingSkills;

    private String recommendations;

    @CreationTimestamp
    private LocalDateTime analyzedAt;

    @ManyToOne
    private Resume resume;

    @ManyToOne
    private Job job;
}
