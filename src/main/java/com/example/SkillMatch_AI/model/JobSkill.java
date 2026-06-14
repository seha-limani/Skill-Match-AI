package com.example.SkillMatch_AI.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "job_skills")
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
