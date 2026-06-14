package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.ApplicationStatus;
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


    private ApplicationStatus status;

    private Double matchScore;

    private LocalDateTime appliedAt;

}
