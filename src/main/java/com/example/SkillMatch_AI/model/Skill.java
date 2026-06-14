package com.example.SkillMatch_AI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

}
