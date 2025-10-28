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

            // Career Objective - prefer enhanced version, fallback to original
            String careerObj = resume.getEnhancedCareerObjective() != null && !resume.getEnhancedCareerObjective().isEmpty()
                ? resume.getEnhancedCareerObjective()
                : resume.getCareerObjective();
            if (careerObj != null && !careerObj.isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Career Objective</h2>");
                html.append("<p>").append(careerObj).append("</p>");
                html.append("</div>");
            }

            // Professional Summary - prefer enhanced version, fallback to original
            String profSummary = resume.getEnhancedProfessionalSummary() != null && !resume.getEnhancedProfessionalSummary().isEmpty()
                ? resume.getEnhancedProfessionalSummary()
                : resume.getProfessionalSummary();
            if (profSummary != null && !profSummary.isEmpty()) {
                html.append("<div class='section'>");
                html.append("<h2>Professional Summary</h2>");
                html.append("<p>").append(profSummary).append("</p>");
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
     * Classic resume template styles - Professional and ATS-friendly
     */
    private String getClassicStyles() {
        return """
            @page {
                margin: 20mm;
            }
            body {
                font-family: 'Calibri', 'Arial', 'Helvetica', sans-serif;
                line-height: 1.6;
                color: #2b2b2b;
                margin: 0;
                padding: 0;
                font-size: 11pt;
            }
            .header {
                border-bottom: 3px solid #1a5490;
                margin-bottom: 25px;
                padding-bottom: 15px;
                text-align: center;
            }
            .header h1 {
                margin: 0 0 10px 0;
                color: #1a5490;
                font-size: 28pt;
                font-weight: 700;
                letter-spacing: 1px;
                text-transform: uppercase;
            }
            .contact-info {
                margin: 8px 0 0 0;
                font-size: 10.5pt;
                color: #555;
                line-height: 1.4;
            }
            .section {
                margin: 20px 0;
                page-break-inside: avoid;
            }
            .section h2 {
                color: #1a5490;
                font-size: 14pt;
                font-weight: 700;
                margin: 15px 0 10px 0;
                padding-bottom: 5px;
                border-bottom: 2px solid #1a5490;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .item {
                margin: 12px 0 12px 0;
                line-height: 1.6;
            }
            .item strong {
                color: #2b2b2b;
                font-size: 11.5pt;
                display: block;
                margin-bottom: 3px;
                font-weight: 600;
            }
            .skills-grid {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                margin-top: 10px;
            }
            .skill-badge {
                background: #f8f9fa;
                border: 1.5px solid #1a5490;
                color: #1a5490;
                padding: 5px 12px;
                border-radius: 3px;
                font-size: 10pt;
                font-weight: 600;
                display: inline-block;
            }
            ul {
                margin: 8px 0;
                padding-left: 20px;
                line-height: 1.7;
            }
            ul li {
                margin: 4px 0;
            }
            p {
                margin: 8px 0;
                text-align: justify;
                line-height: 1.6;
            }
            em {
                color: #666;
                font-size: 10pt;
                font-style: italic;
            }
            br {
                line-height: 1.4;
            }
            """;
    }

    /**
     * Modern resume template styles - Clean and contemporary
     */
    private String getModernStyles() {
        return """
            @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500;700&display=swap');
            body {
                font-family: 'Roboto', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                line-height: 1.7;
                color: #2c3e50;
                margin: 25px 35px;
                font-size: 11pt;
            }
            .header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px;
                border-radius: 10px;
                margin-bottom: 35px;
                text-align: center;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            }
            .header h1 {
                margin: 0;
                font-size: 36px;
                font-weight: 700;
                letter-spacing: 1.5px;
                text-transform: uppercase;
            }
            .contact-info {
                margin: 12px 0 0 0;
                font-size: 12pt;
                opacity: 0.95;
                font-weight: 300;
            }
            .section {
                margin: 28px 0;
                page-break-inside: avoid;
            }
            .section h2 {
                color: #667eea;
                font-size: 18pt;
                margin: 18px 0 12px 0;
                font-weight: 700;
                border-bottom: 3px solid #667eea;
                padding-bottom: 8px;
                text-transform: uppercase;
                letter-spacing: 1px;
            }
            .item {
                margin: 16px 0 16px 15px;
                padding-left: 12px;
                border-left: 3px solid #e8eaf6;
                line-height: 1.8;
            }
            .item strong {
                color: #2c3e50;
                font-size: 13pt;
                display: block;
                margin-bottom: 5px;
                font-weight: 600;
            }
            .skills-grid {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-top: 12px;
            }
            .skill-badge {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 8px 16px;
                border-radius: 25px;
                font-size: 10pt;
                font-weight: 500;
                display: inline-block;
                box-shadow: 0 2px 4px rgba(102, 126, 234, 0.3);
            }
            ul {
                margin: 10px 0;
                padding-left: 25px;
                line-height: 1.9;
            }
            ul li {
                margin: 6px 0;
            }
            p {
                margin: 10px 0;
                text-align: justify;
            }
            em {
                color: #7f8c8d;
                font-size: 10pt;
                font-weight: 300;
            }
            """;
    }

    /**
     * Creative resume template styles - Bold and distinctive
     */
    private String getCreativeStyles() {
        return """
            body {
                font-family: 'Trebuchet MS', 'Helvetica Neue', Arial, sans-serif;
                line-height: 1.8;
                color: #34495e;
                margin: 25px 35px;
                font-size: 11pt;
                background: #fafafa;
            }
            .header {
                background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
                color: white;
                padding: 35px;
                border-radius: 15px;
                margin-bottom: 35px;
                box-shadow: 0 6px 12px rgba(243, 156, 18, 0.3);
                text-align: center;
            }
            .header h1 {
                margin: 0;
                font-size: 38px;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 2px;
                text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
            }
            .contact-info {
                margin: 14px 0 0 0;
                font-size: 12pt;
                opacity: 0.95;
                font-weight: 400;
            }
            .section {
                margin: 28px 0;
                background: white;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
                page-break-inside: avoid;
            }
            .section h2 {
                color: #f39c12;
                font-size: 20pt;
                margin: 0 0 18px 0;
                font-weight: 700;
                text-transform: uppercase;
                letter-spacing: 1.5px;
                border-bottom: 4px solid #f39c12;
                padding-bottom: 8px;
            }
            .item {
                margin: 16px 0;
                padding: 12px;
                background: #f8f9fa;
                border-radius: 8px;
                border-left: 4px solid #f39c12;
                line-height: 1.8;
            }
            .item strong {
                color: #e74c3c;
                font-size: 13pt;
                display: block;
                margin-bottom: 5px;
                font-weight: 700;
            }
            .skills-grid {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-top: 12px;
            }
            .skill-badge {
                background: linear-gradient(135deg, #f39c12 0%, #e67e22 100%);
                color: white;
                padding: 9px 16px;
                border-radius: 25px;
                font-size: 10pt;
                font-weight: 600;
                display: inline-block;
                box-shadow: 0 3px 6px rgba(243, 156, 18, 0.4);
                text-transform: uppercase;
            }
            ul {
                margin: 10px 0;
                padding-left: 25px;
                line-height: 1.9;
            }
            ul li {
                margin: 7px 0;
                color: #2c3e50;
            }
            p {
                margin: 10px 0;
                text-align: justify;
                color: #34495e;
            }
            em {
                color: #95a5a6;
                font-size: 10pt;
                font-style: italic;
            }
            """;
    }
}
