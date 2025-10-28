package com.airesumebuilder.dto;

import com.airesumebuilder.model.*;
import java.util.List;

/**
 * DTO class for ResumeData transfer between frontend and backend.
 * Contains all necessary fields for resume creation and management.
 */
public class ResumeDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String location;
    private String careerObjective;
    private String professionalSummary;
    private String enhancedCareerObjective;
    private String enhancedProfessionalSummary;
    private String enhancedData;
    private Double resumeScore;
    private String resumeScoreFeedback;
    private String template;

    private List<Education> educations;
    private List<Project> projects;
    private List<Skill> skills;
    private List<Certification> certifications;
    private List<Language> languages;
    private List<Achievement> achievements;

    // Constructors
    public ResumeDTO() {
    }

    /**
     * Convert ResumeData entity to DTO
     */
    public static ResumeDTO fromEntity(ResumeData resume) {
        ResumeDTO dto = new ResumeDTO();
        dto.setId(resume.getId());
        dto.setFirstName(resume.getFirstName());
        dto.setLastName(resume.getLastName());
        dto.setEmail(resume.getEmail());
        dto.setPhone(resume.getPhone());
        dto.setLocation(resume.getLocation());
        dto.setCareerObjective(resume.getCareerObjective());
        dto.setProfessionalSummary(resume.getProfessionalSummary());
        dto.setEnhancedCareerObjective(resume.getEnhancedCareerObjective());
        dto.setEnhancedProfessionalSummary(resume.getEnhancedProfessionalSummary());
        dto.setEnhancedData(resume.getEnhancedData());
        dto.setResumeScore(resume.getResumeScore());
        dto.setResumeScoreFeedback(resume.getResumeScoreFeedback());
        dto.setTemplate(resume.getTemplate());
        dto.setEducations(resume.getEducations());
        dto.setProjects(resume.getProjects());
        dto.setSkills(resume.getSkills());
        dto.setCertifications(resume.getCertifications());
        dto.setLanguages(resume.getLanguages());
        dto.setAchievements(resume.getAchievements());
        return dto;
    }

    /**
     * Convert DTO to ResumeData entity
     */
    public ResumeData toEntity() {
        ResumeData resume = new ResumeData();
        resume.setId(this.id);
        resume.setFirstName(this.firstName);
        resume.setLastName(this.lastName);
        resume.setEmail(this.email);
        resume.setPhone(this.phone);
        resume.setLocation(this.location);
        resume.setCareerObjective(this.careerObjective);
        resume.setProfessionalSummary(this.professionalSummary);
        resume.setEnhancedCareerObjective(this.enhancedCareerObjective);
        resume.setEnhancedProfessionalSummary(this.enhancedProfessionalSummary);
        resume.setEnhancedData(this.enhancedData);
        resume.setResumeScore(this.resumeScore);
        resume.setResumeScoreFeedback(this.resumeScoreFeedback);
        resume.setTemplate(this.template);
        resume.setEducations(this.educations);
        resume.setProjects(this.projects);
        resume.setSkills(this.skills);
        resume.setCertifications(this.certifications);
        resume.setLanguages(this.languages);
        resume.setAchievements(this.achievements);
        return resume;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCareerObjective() { return careerObjective; }
    public void setCareerObjective(String careerObjective) { this.careerObjective = careerObjective; }

    public String getProfessionalSummary() { return professionalSummary; }
    public void setProfessionalSummary(String professionalSummary) { this.professionalSummary = professionalSummary; }

    public String getEnhancedCareerObjective() { return enhancedCareerObjective; }
    public void setEnhancedCareerObjective(String enhancedCareerObjective) { this.enhancedCareerObjective = enhancedCareerObjective; }

    public String getEnhancedProfessionalSummary() { return enhancedProfessionalSummary; }
    public void setEnhancedProfessionalSummary(String enhancedProfessionalSummary) { this.enhancedProfessionalSummary = enhancedProfessionalSummary; }

    public String getEnhancedData() { return enhancedData; }
    public void setEnhancedData(String enhancedData) { this.enhancedData = enhancedData; }

    public Double getResumeScore() { return resumeScore; }
    public void setResumeScore(Double resumeScore) { this.resumeScore = resumeScore; }

    public String getResumeScoreFeedback() { return resumeScoreFeedback; }
    public void setResumeScoreFeedback(String resumeScoreFeedback) { this.resumeScoreFeedback = resumeScoreFeedback; }

    public String getTemplate() { return template; }
    public void setTemplate(String template) { this.template = template; }

    public List<Education> getEducations() { return educations; }
    public void setEducations(List<Education> educations) { this.educations = educations; }

    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }

    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }

    public List<Certification> getCertifications() { return certifications; }
    public void setCertifications(List<Certification> certifications) { this.certifications = certifications; }

    public List<Language> getLanguages() { return languages; }
    public void setLanguages(List<Language> languages) { this.languages = languages; }

    public List<Achievement> getAchievements() { return achievements; }
    public void setAchievements(List<Achievement> achievements) { this.achievements = achievements; }
}
