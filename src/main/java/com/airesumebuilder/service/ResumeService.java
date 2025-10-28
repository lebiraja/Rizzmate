package com.airesumebuilder.service;

import com.airesumebuilder.dto.EnhancementRequestDTO;
import com.airesumebuilder.dto.ResumeDTO;
import com.airesumebuilder.model.ResumeData;
import com.airesumebuilder.repository.ResumeRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for resume management.
 * Handles all business logic for resume creation, retrieval, and enhancement.
 *
 * Features:
 * - CRUD operations for resume data
 * - Integration with GeminiService for AI enhancement
 * - Integration with PdfService for PDF generation
 * - Resume enhancement and scoring
 */
@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private PdfService pdfService;

    private final Gson gson = new Gson();

    /**
     * Create a new resume
     */
    @Transactional
    public ResumeDTO createResume(ResumeDTO resumeDTO) {
        try {
            ResumeData resume = resumeDTO.toEntity();
            ResumeData savedResume = resumeRepository.save(resume);
            log.info("Resume created with ID: {}", savedResume.getId());
            return ResumeDTO.fromEntity(savedResume);
        } catch (Exception e) {
            log.error("Error creating resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create resume: " + e.getMessage());
        }
    }

    /**
     * Get resume by ID
     */
    public ResumeDTO getResumeById(Long id) {
        try {
            return resumeRepository.findById(id)
                    .map(ResumeDTO::fromEntity)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + id));
        } catch (Exception e) {
            log.error("Error retrieving resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to retrieve resume: " + e.getMessage());
        }
    }

    /**
     * Update resume
     */
    @Transactional
    public ResumeDTO updateResume(Long id, ResumeDTO resumeDTO) {
        try {
            ResumeData resume = resumeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + id));

            // Update basic fields
            if (resumeDTO.getFirstName() != null) resume.setFirstName(resumeDTO.getFirstName());
            if (resumeDTO.getLastName() != null) resume.setLastName(resumeDTO.getLastName());
            if (resumeDTO.getEmail() != null) resume.setEmail(resumeDTO.getEmail());
            if (resumeDTO.getPhone() != null) resume.setPhone(resumeDTO.getPhone());
            if (resumeDTO.getLocation() != null) resume.setLocation(resumeDTO.getLocation());
            if (resumeDTO.getCareerObjective() != null) resume.setCareerObjective(resumeDTO.getCareerObjective());
            if (resumeDTO.getProfessionalSummary() != null) resume.setProfessionalSummary(resumeDTO.getProfessionalSummary());
            if (resumeDTO.getTemplate() != null) resume.setTemplate(resumeDTO.getTemplate());

            // Update relationships
            if (resumeDTO.getEducations() != null) resume.setEducations(resumeDTO.getEducations());
            if (resumeDTO.getProjects() != null) resume.setProjects(resumeDTO.getProjects());
            if (resumeDTO.getSkills() != null) resume.setSkills(resumeDTO.getSkills());
            if (resumeDTO.getCertifications() != null) resume.setCertifications(resumeDTO.getCertifications());
            if (resumeDTO.getLanguages() != null) resume.setLanguages(resumeDTO.getLanguages());
            if (resumeDTO.getAchievements() != null) resume.setAchievements(resumeDTO.getAchievements());

            ResumeData updatedResume = resumeRepository.save(resume);
            log.info("Resume updated with ID: {}", id);
            return ResumeDTO.fromEntity(updatedResume);
        } catch (Exception e) {
            log.error("Error updating resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update resume: " + e.getMessage());
        }
    }

    /**
     * Enhance resume using Gemini API
     */
    @Transactional
    public ResumeDTO enhanceResume(Long id, EnhancementRequestDTO request) {
        try {
            ResumeData resume = resumeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + id));

            // Call Gemini API for enhancement
            String enhancedContent = geminiService.enhanceResumeContent(request);

            // Parse enhanced content
            try {
                JsonObject enhancedJson = gson.fromJson(enhancedContent, JsonObject.class);
                
                if (enhancedJson.has("enhancedCareerObjective")) {
                    resume.setEnhancedCareerObjective(
                        enhancedJson.get("enhancedCareerObjective").getAsString()
                    );
                }
                if (enhancedJson.has("enhancedProfessionalSummary")) {
                    resume.setEnhancedProfessionalSummary(
                        enhancedJson.get("enhancedProfessionalSummary").getAsString()
                    );
                }
            } catch (Exception e) {
                log.warn("Could not parse enhanced JSON, storing raw response");
                resume.setEnhancedData(enhancedContent);
            }

            ResumeData updatedResume = resumeRepository.save(resume);
            log.info("Resume enhanced with ID: {}", id);
            return ResumeDTO.fromEntity(updatedResume);
        } catch (Exception e) {
            log.error("Error enhancing resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to enhance resume: " + e.getMessage());
        }
    }

    /**
     * Calculate resume score using Gemini API
     */
    @Transactional
    public ResumeDTO calculateResumeScore(Long id, EnhancementRequestDTO request) {
        try {
            ResumeData resume = resumeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + id));

            // Call Gemini API for scoring
            String scoreResponse = geminiService.calculateResumeScore(request);

            // Parse score response
            try {
                JsonObject scoreJson = gson.fromJson(scoreResponse, JsonObject.class);
                
                if (scoreJson.has("score")) {
                    resume.setResumeScore(scoreJson.get("score").getAsDouble());
                }
                if (scoreJson.has("feedback")) {
                    resume.setResumeScoreFeedback(scoreJson.get("feedback").getAsString());
                }
            } catch (Exception e) {
                log.warn("Could not parse score JSON, storing raw response");
                resume.setResumeScoreFeedback(scoreResponse);
            }

            ResumeData updatedResume = resumeRepository.save(resume);
            log.info("Resume score calculated for ID: {}", id);
            return ResumeDTO.fromEntity(updatedResume);
        } catch (Exception e) {
            log.error("Error calculating resume score: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to calculate resume score: " + e.getMessage());
        }
    }

    /**
     * Generate PDF from resume data
     */
    @Transactional
    public byte[] generateResumePdf(Long id) {
        try {
            ResumeData resume = resumeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Resume not found with ID: " + id));

            // Build HTML content
            String htmlContent = pdfService.buildResumeHtml(resume);

            // Generate PDF
            byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

            // Update generation timestamp
            resume.setGeneratedAt(java.time.LocalDateTime.now());
            resumeRepository.save(resume);

            log.info("PDF generated for resume ID: {}", id);
            return pdfBytes;
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }

    /**
     * Delete resume by ID
     */
    @Transactional
    public void deleteResume(Long id) {
        try {
            if (!resumeRepository.existsById(id)) {
                throw new RuntimeException("Resume not found with ID: " + id);
            }
            resumeRepository.deleteById(id);
            log.info("Resume deleted with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting resume: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete resume: " + e.getMessage());
        }
    }
}
