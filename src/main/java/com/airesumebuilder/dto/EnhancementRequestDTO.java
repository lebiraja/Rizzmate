package com.airesumebuilder.dto;

/**
 * DTO for Gemini API enhancement requests.
 * Represents data sent to Gemini API for AI-powered text enhancement.
 */
public class EnhancementRequestDTO {

    private Long resumeId;
    private String careerObjective;
    private String professionalSummary;
    private String skillsDescription;
    private String projectDescriptions;
    private String achievementsDescription;

    // Constructors
    public EnhancementRequestDTO() {
    }

    public EnhancementRequestDTO(Long resumeId, String careerObjective, String professionalSummary,
                                 String skillsDescription, String projectDescriptions, String achievementsDescription) {
        this.resumeId = resumeId;
        this.careerObjective = careerObjective;
        this.professionalSummary = professionalSummary;
        this.skillsDescription = skillsDescription;
        this.projectDescriptions = projectDescriptions;
        this.achievementsDescription = achievementsDescription;
    }

    /**
     * Get the complete text to be enhanced
     */
    public String getCombinedText() {
        StringBuilder sb = new StringBuilder();
        if (careerObjective != null && !careerObjective.isEmpty()) {
            sb.append("Career Objective: ").append(careerObjective).append("\n");
        }
        if (professionalSummary != null && !professionalSummary.isEmpty()) {
            sb.append("Professional Summary: ").append(professionalSummary).append("\n");
        }
        if (skillsDescription != null && !skillsDescription.isEmpty()) {
            sb.append("Skills: ").append(skillsDescription).append("\n");
        }
        if (projectDescriptions != null && !projectDescriptions.isEmpty()) {
            sb.append("Projects: ").append(projectDescriptions).append("\n");
        }
        if (achievementsDescription != null && !achievementsDescription.isEmpty()) {
            sb.append("Achievements: ").append(achievementsDescription).append("\n");
        }
        return sb.toString();
    }

    // Getters and Setters
    public Long getResumeId() { return resumeId; }
    public void setResumeId(Long resumeId) { this.resumeId = resumeId; }

    public String getCareerObjective() { return careerObjective; }
    public void setCareerObjective(String careerObjective) { this.careerObjective = careerObjective; }

    public String getProfessionalSummary() { return professionalSummary; }
    public void setProfessionalSummary(String professionalSummary) { this.professionalSummary = professionalSummary; }

    public String getSkillsDescription() { return skillsDescription; }
    public void setSkillsDescription(String skillsDescription) { this.skillsDescription = skillsDescription; }

    public String getProjectDescriptions() { return projectDescriptions; }
    public void setProjectDescriptions(String projectDescriptions) { this.projectDescriptions = projectDescriptions; }

    public String getAchievementsDescription() { return achievementsDescription; }
    public void setAchievementsDescription(String achievementsDescription) { this.achievementsDescription = achievementsDescription; }
}
