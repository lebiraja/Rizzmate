package com.airesumebuilder.controller;

import com.airesumebuilder.dto.ApiResponse;
import com.airesumebuilder.dto.EnhancementRequestDTO;
import com.airesumebuilder.dto.ResumeDTO;
import com.airesumebuilder.service.ResumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Resume API endpoints.
 * Handles all HTTP requests related to resume creation, enhancement, and PDF generation.
 *
 * Endpoints:
 * - POST /api/resume/submit → Create and save resume
 * - GET /api/resume/{id} → Retrieve resume by ID
 * - PUT /api/resume/{id} → Update resume
 * - POST /api/resume/{id}/enhance → Enhance resume with AI
 * - POST /api/resume/{id}/score → Calculate resume score
 * - GET /api/resume/{id}/pdf → Generate and download PDF
 * - DELETE /api/resume/{id} → Delete resume
 */
@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    @Autowired
    private ResumeService resumeService;

    /**
     * POST /api/resume/submit
     * Create and save a new resume
     */
    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<ResumeDTO>> submitResume(@RequestBody ResumeDTO resumeDTO) {
        try {
            log.info("Submitting new resume for: {}", resumeDTO.getEmail());
            ResumeDTO savedResume = resumeService.createResume(resumeDTO);
            return ResponseEntity.ok(
                ApiResponse.success("Resume submitted successfully", savedResume)
            );
        } catch (Exception e) {
            log.error("Error submitting resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to submit resume", e.getMessage()));
        }
    }

    /**
     * GET /api/resume/{id}
     * Retrieve resume by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDTO>> getResume(@PathVariable Long id) {
        try {
            log.info("Retrieving resume with ID: {}", id);
            ResumeDTO resume = resumeService.getResumeById(id);
            return ResponseEntity.ok(
                ApiResponse.success("Resume retrieved successfully", resume)
            );
        } catch (Exception e) {
            log.error("Error retrieving resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to retrieve resume", e.getMessage()));
        }
    }

    /**
     * PUT /api/resume/{id}
     * Update an existing resume
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeDTO>> updateResume(
            @PathVariable Long id,
            @RequestBody ResumeDTO resumeDTO) {
        try {
            log.info("Updating resume with ID: {}", id);
            ResumeDTO updatedResume = resumeService.updateResume(id, resumeDTO);
            return ResponseEntity.ok(
                ApiResponse.success("Resume updated successfully", updatedResume)
            );
        } catch (Exception e) {
            log.error("Error updating resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to update resume", e.getMessage()));
        }
    }

    /**
     * POST /api/resume/{id}/enhance
     * Enhance resume using Gemini AI
     */
    @PostMapping("/{id}/enhance")
    public ResponseEntity<ApiResponse<ResumeDTO>> enhanceResume(
            @PathVariable Long id,
            @RequestBody EnhancementRequestDTO request) {
        try {
            log.info("Enhancing resume with ID: {}", id);
            ResumeDTO enhancedResume = resumeService.enhanceResume(id, request);
            return ResponseEntity.ok(
                ApiResponse.success("Resume enhanced successfully with AI", enhancedResume)
            );
        } catch (Exception e) {
            log.error("Error enhancing resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to enhance resume", e.getMessage()));
        }
    }

    /**
     * POST /api/resume/{id}/score
     * Calculate resume score using Gemini AI
     */
    @PostMapping("/{id}/score")
    public ResponseEntity<ApiResponse<ResumeDTO>> calculateScore(
            @PathVariable Long id,
            @RequestBody EnhancementRequestDTO request) {
        try {
            log.info("Calculating score for resume with ID: {}", id);
            ResumeDTO scoredResume = resumeService.calculateResumeScore(id, request);
            return ResponseEntity.ok(
                ApiResponse.success("Resume score calculated successfully", scoredResume)
            );
        } catch (Exception e) {
            log.error("Error calculating resume score: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to calculate resume score", e.getMessage()));
        }
    }

    /**
     * GET /api/resume/{id}/pdf
     * Generate and download resume as PDF
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        try {
            log.info("Generating PDF for resume with ID: {}", id);
            byte[] pdfBytes = resumeService.generateResumePdf(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume_" + id + ".pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            log.error("Error generating PDF: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/resume/{id}
     * Delete a resume
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteResume(@PathVariable Long id) {
        try {
            log.info("Deleting resume with ID: {}", id);
            resumeService.deleteResume(id);
            return ResponseEntity.ok(
                ApiResponse.success("Resume deleted successfully", "Resume with ID " + id + " has been deleted")
            );
        } catch (Exception e) {
            log.error("Error deleting resume: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Failed to delete resume", e.getMessage()));
        }
    }
}
