package com.example.SkillMatch_AI.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "savd_jobs")
public class SavedJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
