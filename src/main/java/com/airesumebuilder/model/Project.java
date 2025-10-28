package com.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a user's project.
 * Maps to the project table in the database.
 */
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String description;

    @Column
    private String technologies;

    @Column
    private String projectLink;

    @Column(length = 500)
    private String achievements;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore  // Prevent infinite recursion during JSON serialization
    private ResumeData resume;

    // Constructors
    public Project() {
    }

    public Project(Long id, String projectName, String description, String technologies,
                   String projectLink, String achievements, String startDate, String endDate, ResumeData resume) {
        this.id = id;
        this.projectName = projectName;
        this.description = description;
        this.technologies = technologies;
        this.projectLink = projectLink;
        this.achievements = achievements;
        this.startDate = startDate;
        this.endDate = endDate;
        this.resume = resume;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getProjectLink() {
        return projectLink;
    }

    public void setProjectLink(String projectLink) {
        this.projectLink = projectLink;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ResumeData getResume() {
        return resume;
    }

    public void setResume(ResumeData resume) {
        this.resume = resume;
    }
}
