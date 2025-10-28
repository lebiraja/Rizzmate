package com.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a user's skill.
 * Maps to the skill table in the database.
 */
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String skillName;

    @Column
    private String proficiency;

    @Column(length = 500)
    private String description;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore  // Prevent infinite recursion during JSON serialization
    private ResumeData resume;

    // Constructors
    public Skill() {
    }

    public Skill(Long id, String skillName, String proficiency, String description, ResumeData resume) {
        this.id = id;
        this.skillName = skillName;
        this.proficiency = proficiency;
        this.description = description;
        this.resume = resume;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ResumeData getResume() {
        return resume;
    }

    public void setResume(ResumeData resume) {
        this.resume = resume;
    }
}
