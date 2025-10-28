package com.airesumebuilder.controller;

import com.airesumebuilder.dto.ApiResponse;
import com.airesumebuilder.dto.EnhancementRequestDTO;
import com.airesumebuilder.dto.ResumeDTO;
import com.airesumebuilder.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, maxAge = 3600)
@Tag(name = "Resume Management", description = "APIs for resume creation, enhancement, and management")
public class ResumeController {

    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

    @Autowired
    private ResumeService resumeService;

    /**
     * POST /api/resume/submit
     * Create and save a new resume
     */
    @PostMapping("/submit")
    @Operation(
        summary = "Create new resume",
        description = "Accepts validated resume data and saves to database"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume created successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid resume data"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ApiResponse<ResumeDTO>> submitResume(
            @Parameter(description = "Resume data to create", required = true)
            @Valid @RequestBody ResumeDTO resumeDTO) {
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
    @Operation(
        summary = "Get resume by ID",
        description = "Retrieves a resume with all details including educations, projects, skills, etc."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found")
    })
    public ResponseEntity<ApiResponse<ResumeDTO>> getResume(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id) {
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
    @Operation(
        summary = "Update resume",
        description = "Updates an existing resume with new data"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid resume data")
    })
    public ResponseEntity<ApiResponse<ResumeDTO>> updateResume(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated resume data", required = true)
            @Valid @RequestBody ResumeDTO resumeDTO) {
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
    @Operation(
        summary = "Enhance resume with AI",
        description = "Uses Google Gemini AI to improve resume content with better wording and professional language. Rate limited to 10 requests per minute."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume enhanced successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "Rate limit exceeded - max 10 requests per minute"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "AI service unavailable")
    })
    public ResponseEntity<ApiResponse<ResumeDTO>> enhanceResume(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Content to enhance", required = true)
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
    @Operation(
        summary = "Calculate resume score",
        description = "Analyzes resume and provides a score out of 100 based on content quality, presentation, skills, projects, and marketability. Rate limited to 10 requests per minute."
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Score calculated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "429", description = "Rate limit exceeded"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "AI service unavailable")
    })
    public ResponseEntity<ApiResponse<ResumeDTO>> calculateScore(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Resume content to score", required = true)
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
    @Operation(
        summary = "Download resume as PDF",
        description = "Generates a professionally formatted PDF document of the resume"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "PDF generated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "PDF generation failed")
    })
    public ResponseEntity<byte[]> downloadPdf(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id) {
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
    @Operation(
        summary = "Delete resume",
        description = "Permanently deletes a resume and all associated data"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Resume deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Resume not found")
    })
    public ResponseEntity<ApiResponse<String>> deleteResume(
            @Parameter(description = "Resume ID", required = true)
            @PathVariable Long id) {
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
