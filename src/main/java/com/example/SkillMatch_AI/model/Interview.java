package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.InterviewStatus;
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

    private InterviewStatus status;

    private String notes;
}
