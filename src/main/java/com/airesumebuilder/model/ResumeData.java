package com.airesumebuilder.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Main entity class representing user resume data.
 * This is the core model that stores all resume-related information.
 * Maps to the resume_data table in the database.
 *
 * Contains relationships to Education, Project, Skill, and other entities.
 */
@Entity
@Table(name = "resume_data")
public class ResumeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Information
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column
    private String location;

    @Column
    private String profilePhoto;

    // Professional Information
    @Column(length = 1000)
    private String careerObjective;

    @Column(length = 2000)
    private String professionalSummary;

    // Enhanced Data (After AI processing)
    @Column(columnDefinition = "TEXT")
    private String enhancedCareerObjective;

    @Column(columnDefinition = "TEXT")
    private String enhancedProfessionalSummary;

    @Column(columnDefinition = "TEXT")
    private String enhancedData;

    // Resume Score
    @Column
    private Double resumeScore;

    @Column(length = 1000)
    private String resumeScoreFeedback;

    // Template Selection
    @Column
    private String template = "classic";

    // Metadata
    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime generatedAt;

    // Relationships
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Education> educations;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certification> certifications;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Language> languages;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Achievement> achievements;

    // Constructors
    public ResumeData() {
    }

    /**
     * Initializes the createdAt and updatedAt timestamps before inserting
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Updates the updatedAt timestamp before updating the entity
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Get the full name of the user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getCareerObjective() {
        return careerObjective;
    }

    public void setCareerObjective(String careerObjective) {
        this.careerObjective = careerObjective;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public String getEnhancedCareerObjective() {
        return enhancedCareerObjective;
    }

    public void setEnhancedCareerObjective(String enhancedCareerObjective) {
        this.enhancedCareerObjective = enhancedCareerObjective;
    }

    public String getEnhancedProfessionalSummary() {
        return enhancedProfessionalSummary;
    }

    public void setEnhancedProfessionalSummary(String enhancedProfessionalSummary) {
        this.enhancedProfessionalSummary = enhancedProfessionalSummary;
    }

    public String getEnhancedData() {
        return enhancedData;
    }

    public void setEnhancedData(String enhancedData) {
        this.enhancedData = enhancedData;
    }

    public Double getResumeScore() {
        return resumeScore;
    }

    public void setResumeScore(Double resumeScore) {
        this.resumeScore = resumeScore;
    }

    public String getResumeScoreFeedback() {
        return resumeScoreFeedback;
    }

    public void setResumeScoreFeedback(String resumeScoreFeedback) {
        this.resumeScoreFeedback = resumeScoreFeedback;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
        // Set bidirectional relationship
        if (educations != null) {
            educations.forEach(education -> education.setResume(this));
        }
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        // Set bidirectional relationship
        if (projects != null) {
            projects.forEach(project -> project.setResume(this));
        }
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
        // Set bidirectional relationship
        if (skills != null) {
            skills.forEach(skill -> skill.setResume(this));
        }
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
        // Set bidirectional relationship
        if (certifications != null) {
            certifications.forEach(certification -> certification.setResume(this));
        }
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
        // Set bidirectional relationship
        if (languages != null) {
            languages.forEach(language -> language.setResume(this));
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public void setAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
        // Set bidirectional relationship
        if (achievements != null) {
            achievements.forEach(achievement -> achievement.setResume(this));
        }
    }
}
