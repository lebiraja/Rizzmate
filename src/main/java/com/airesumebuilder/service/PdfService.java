package com.airesumebuilder.service;

import com.airesumebuilder.model.ResumeData;
import com.itextpdf.html2pdf.HtmlConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

/**
 * Service class for PDF generation.
 * Handles conversion of Thymeleaf HTML templates to PDF files.
 *
 * Features:
 * - Convert HTML template to PDF
 * - Apply styling and formatting
 * - Support for multiple resume templates
 */
@Service
public class PdfService {

    private static final Logger log = LoggerFactory.getLogger(PdfService.class);

    /**
     * Generate PDF from HTML content
     */
    public byte[] generatePdfFromHtml(String htmlContent) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            
            // Ensure proper HTML structure
            String fullHtml = htmlContent;
            if (!htmlContent.toLowerCase().contains("<html")) {
                fullHtml = "<!DOCTYPE html><html><head><meta charset='UTF-8'></head><body>" + 
                          htmlContent + 
                          "</body></html>";
            }
            
            // Convert HTML to PDF using iText
            HtmlConverter.convertToPdf(fullHtml, outputStream);
            
            byte[] pdfBytes = outputStream.toByteArray();
            log.info("Successfully generated PDF, size: {} bytes", pdfBytes.length);
            
            return pdfBytes;
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }

    /**
     * Build HTML content for resume from resume data
     */
    public String buildResumeHtml(ResumeData resume) {
        try {
            StringBuilder html = new StringBuilder();
            
            // HTML Header and Styling
            html.append("<html><head><meta charset='UTF-8'>");
            html.append("<style>");
            html.append(getResumeStyles(resume.getTemplate()));
            html.append("</style></head><body>");

            // Personal Information
            html.append("<div class='header'>");
            html.append("<h1>").append(resume.getFullName()).append("</h1>");
            html.append("<p class='contact-info'>");
            html.append(resume.getEmail()).append(" | ");
            html.append(resume.getPhone()).append(" | ");
            if (resume.getLocation() != null) {
                html.append(resume.getLocation());
            }
            html.append("</p>");
            html.append("</div>");

            // Career Objective
            if (resume.getEnhancedCareerObjective() != null && !resume.getEnhancedCareerObjective().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Career Objective</h2>");
                html.append("<p>").append(resume.getEnhancedCareerObjective()).append("</p>");
                html.append("</div>");
            }

            // Professional Summary
            if (resume.getEnhancedProfessionalSummary() != null && !resume.getEnhancedProfessionalSummary().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Professional Summary</h2>");
                html.append("<p>").append(resume.getEnhancedProfessionalSummary()).append("</p>");
                html.append("</div>");
            }

            // Education
            if (resume.getEducations() != null && !resume.getEducations().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Education</h2>");
                resume.getEducations().forEach(edu -> {
                    html.append("<div class='item'>");
                    html.append("<strong>").append(edu.getDegree()).append(" in ").append(edu.getFieldOfStudy())
                        .append("</strong> - ").append(edu.getUniversity()).append(" (")
                        .append(edu.getGraduationYear()).append(")");
                    if (edu.getCgpa() != null) {
                        html.append("<br/>CGPA: ").append(edu.getCgpa());
                    }
                    html.append("</div>");
                });
                html.append("</div>");
            }

            // Skills
            if (resume.getSkills() != null && !resume.getSkills().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Skills</h2>");
                html.append("<div class='skills-grid'>");
                resume.getSkills().forEach(skill -> {
                    html.append("<span class='skill-badge'>").append(skill.getSkillName());
                    if (skill.getProficiency() != null) {
                        html.append(" (").append(skill.getProficiency()).append(")");
                    }
                    html.append("</span>");
                });
                html.append("</div>");
                html.append("</div>");
            }

            // Projects
            if (resume.getProjects() != null && !resume.getProjects().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Projects</h2>");
                resume.getProjects().forEach(project -> {
                    html.append("<div class='item'>");
                    html.append("<strong>").append(project.getProjectName()).append("</strong>");
                    if (project.getStartDate() != null && project.getEndDate() != null) {
                        html.append(" (").append(project.getStartDate()).append(" - ")
                            .append(project.getEndDate()).append(")");
                    }
                    if (project.getDescription() != null) {
                        html.append("<br/>").append(project.getDescription());
                    }
                    if (project.getTechnologies() != null) {
                        html.append("<br/><em>Technologies: ").append(project.getTechnologies()).append("</em>");
                    }
                    html.append("</div>");
                });
                html.append("</div>");
            }

            // Certifications
            if (resume.getCertifications() != null && !resume.getCertifications().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Certifications</h2>");
                resume.getCertifications().forEach(cert -> {
                    html.append("<div class='item'>");
                    html.append("<strong>").append(cert.getCertificationName()).append("</strong>");
                    if (cert.getIssuer() != null) {
                        html.append(" - ").append(cert.getIssuer());
                    }
                    if (cert.getIssueDate() != null) {
                        html.append(" (").append(cert.getIssueDate()).append(")");
                    }
                    html.append("</div>");
                });
                html.append("</div>");
            }

            // Languages
            if (resume.getLanguages() != null && !resume.getLanguages().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Languages</h2>");
                html.append("<p>");
                for (int i = 0; i < resume.getLanguages().size(); i++) {
                    if (i > 0) html.append(", ");
                    var lang = resume.getLanguages().get(i);
                    html.append(lang.getLanguageName());
                    if (lang.getProficiency() != null) {
                        html.append(" (").append(lang.getProficiency()).append(")");
                    }
                }
                html.append("</p>");
                html.append("</div>");
            }

            // Achievements
            if (resume.getAchievements() != null && !resume.getAchievements().isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Achievements</h2>");
                html.append("<ul>");
                resume.getAchievements().forEach(achievement -> {
                    html.append("<li>").append(achievement.getAchievementTitle());
                    if (achievement.getDescription() != null) {
                        html.append(": ").append(achievement.getDescription());
                    }
                    html.append("</li>");
                });
                html.append("</ul>");
                html.append("</div>");
            }

            html.append("</body></html>");
            
            return html.toString();
        } catch (Exception e) {
            log.error("Error building resume HTML: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to build resume HTML: " + e.getMessage());
        }
    }

    /**
     * Get CSS styles based on template selection
     */
    private String getResumeStyles(String template) {
        if ("modern".equalsIgnoreCase(template)) {
            return getModernStyles();
        } else if ("creative".equalsIgnoreCase(template)) {
            return getCreativeStyles();
        } else {
            return getClassicStyles();
        }
    }

    /**
     * Classic resume template styles
     */
    private String getClassicStyles() {
        return "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 20px; } " +
               ".header { border-bottom: 2px solid #0066cc; margin-bottom: 20px; padding-bottom: 10px; } " +
               ".header h1 { margin: 0; color: #0066cc; font-size: 28px; } " +
               ".contact-info { margin: 5px 0 0 0; font-size: 12px; color: #666; } " +
               ".section { margin: 20px 0; } " +
               ".section h2 { border-left: 4px solid #0066cc; padding-left: 10px; font-size: 16px; margin: 10px 0 5px 0; } " +
               ".item { margin: 10px 0; } " +
               ".item strong { color: #0066cc; } " +
               ".skills-grid { display: flex; flex-wrap: wrap; gap: 8px; } " +
               ".skill-badge { background: #f0f0f0; padding: 5px 10px; border-radius: 3px; font-size: 12px; } " +
               "ul { margin: 5px 0; padding-left: 20px; }";
    }

    /**
     * Modern resume template styles
     */
    private String getModernStyles() {
        return "body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.7; color: #2c3e50; margin: 20px; } " +
               ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 25px; border-radius: 8px; margin-bottom: 30px; } " +
               ".header h1 { margin: 0; font-size: 32px; font-weight: 600; } " +
               ".contact-info { margin: 10px 0 0 0; font-size: 13px; opacity: 0.9; } " +
               ".section { margin: 25px 0; } " +
               ".section h2 { color: #667eea; font-size: 18px; margin: 15px 0 10px 0; font-weight: 600; border-bottom: 2px solid #667eea; padding-bottom: 5px; } " +
               ".item { margin: 12px 0; padding-left: 10px; } " +
               ".item strong { color: #2c3e50; font-size: 14px; } " +
               ".skills-grid { display: flex; flex-wrap: wrap; gap: 10px; } " +
               ".skill-badge { background: #f8f9fa; border: 1px solid #667eea; color: #667eea; padding: 8px 12px; border-radius: 20px; font-size: 12px; font-weight: 500; } " +
               "ul { margin: 5px 0; padding-left: 20px; }";
    }

    /**
     * Creative resume template styles
     */
    private String getCreativeStyles() {
        return "body { font-family: 'Trebuchet MS', sans-serif; line-height: 1.8; color: #34495e; margin: 20px; } " +
               ".header { background: #f39c12; color: white; padding: 30px; border-radius: 15px; margin-bottom: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); } " +
               ".header h1 { margin: 0; font-size: 36px; font-weight: 700; text-transform: uppercase; } " +
               ".contact-info { margin: 12px 0 0 0; font-size: 14px; opacity: 0.95; } " +
               ".section { margin: 25px 0; background: #ecf0f1; padding: 15px; border-radius: 8px; } " +
               ".section h2 { color: #f39c12; font-size: 20px; margin: 0 0 15px 0; font-weight: 700; text-transform: uppercase; } " +
               ".item { margin: 12px 0; } " +
               ".item strong { color: #e74c3c; } " +
               ".skills-grid { display: flex; flex-wrap: wrap; gap: 10px; } " +
               ".skill-badge { background: #f39c12; color: white; padding: 8px 14px; border-radius: 25px; font-size: 12px; font-weight: 600; } " +
               "ul { margin: 5px 0; padding-left: 20px; }";
    }
}
