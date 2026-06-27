package com.example.SkillMatch_AI.model;

import com.example.SkillMatch_AI.model.Enum.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
@JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences", "educations", "notifications", "sentMessages", "receivedMessages", "savedJobs"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String firstName;


    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(nullable = false)
    private String lastName;


    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    @JsonIgnore
    private String password;


    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must contain exactly 10 digits"
    )
    @Column(nullable = false)
    private String phone;

    private String profileImage;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;


    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    private Role role;



    @OneToMany
    private List<Company> companies;

    @OneToMany
    private List<Job> jobs;

    @OneToMany
    private List<Resume> resumes;

    @OneToMany
    private List<Experience> experiences;

    @OneToMany
    private List<Education> educations;

    @OneToMany
    private List<Notification> notifications;

    @OneToMany
    private List<Message> sentMessages;

    @OneToMany
    private List<Message> receivedMessages;

    @OneToMany
    private List<SavedJob> savedJobs;
}
