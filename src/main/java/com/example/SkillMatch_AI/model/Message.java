package com.example.SkillMatch_AI.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String content;

    private LocalDateTime sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
    private User receiver;

    @Column(length = 100)
    private String conversationKey;

    private LocalDateTime readAt;

    @PrePersist
    void onCreate() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}
