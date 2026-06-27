package com.example.SkillMatch_AI.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "companies")
@JsonIgnoreProperties({"jobs"})
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String name;


    @NotBlank
    @Size(min = 20, max = 2000, message = "Description must be between 20 and 2000 characters")
    @Column(nullable = false, length = 2000)
    private String description;

    @NotBlank(message = "Website is required")
    @Pattern(
            regexp = "^(https?:\\/\\/)?([\\w\\-]+\\.)+[a-zA-Z]{2,}(\\/.*)?$",
            message = "Please enter a valid website URL"
    )
    private String website;

    private String logo;

    private String location;


    private Boolean verified = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @NotNull(message = "Company owner is required")
    @JsonIgnoreProperties({"companies", "jobs", "resumes", "experiences"})
    private User user;

    @OneToMany
    private List<Job> jobs;
}
