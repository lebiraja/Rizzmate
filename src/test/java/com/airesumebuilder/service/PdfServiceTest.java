package com.airesumebuilder.service;

import com.airesumebuilder.model.ResumeData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PdfService
 * 
 * Tests cover:
 * - PDF generation from HTML
 * - Resume HTML building with different templates
 * - Handling of different resume sections
 * - Error handling
 */
@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @InjectMocks
    private PdfService pdfService;

    private ResumeData sampleResume;

    @BeforeEach
    void setUp() {
        // Initialize sample resume data
        sampleResume = new ResumeData();
        sampleResume.setId(1L);
        sampleResume.setFirstName("John");
        sampleResume.setLastName("Doe");
        sampleResume.setEmail("john.doe@example.com");
        sampleResume.setPhone("1234567890");
        sampleResume.setLocation("Mumbai, India");
        sampleResume.setCareerObjective("Seeking entry-level software development position");
        sampleResume.setTemplate("classic");
        sampleResume.setEducations(new ArrayList<>());
        sampleResume.setProjects(new ArrayList<>());
        sampleResume.setSkills(new ArrayList<>());
        sampleResume.setCertifications(new ArrayList<>());
        sampleResume.setLanguages(new ArrayList<>());
        sampleResume.setAchievements(new ArrayList<>());
    }

    @Test
    void testBuildResumeHtml_Classic_Success() {
        // Arrange
        sampleResume.setTemplate("classic");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("John Doe"));
        assertTrue(html.contains("john.doe@example.com"));
        assertTrue(html.contains("1234567890"));
        assertTrue(html.contains("Mumbai, India"));
        assertTrue(html.contains("Career Objective"));
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
        assertTrue(html.contains("#1a5490")); // Classic template color
    }

    @Test
    void testBuildResumeHtml_Modern_Success() {
        // Arrange
        sampleResume.setTemplate("modern");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("John Doe"));
        assertTrue(html.contains("linear-gradient")); // Modern template uses gradients
        assertTrue(html.contains("#667eea")); // Modern template color
    }

    @Test
    void testBuildResumeHtml_Creative_Success() {
        // Arrange
        sampleResume.setTemplate("creative");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("John Doe"));
        assertTrue(html.contains("#f39c12")); // Creative template color
    }

    @Test
    void testBuildResumeHtml_DefaultsToClassic() {
        // Arrange
        sampleResume.setTemplate(null);

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("John Doe"));
        // Classic template characteristics
        assertTrue(html.contains("#1a5490"));
    }

    @Test
    void testBuildResumeHtml_WithEnhancedContent() {
        // Arrange
        sampleResume.setEnhancedCareerObjective("Enhanced career objective with professional language");
        sampleResume.setEnhancedProfessionalSummary("Enhanced professional summary");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("Enhanced career objective"));
        assertTrue(html.contains("Enhanced professional summary"));
    }

    @Test
    void testGeneratePdfFromHtml_ValidHtml_Success() {
        // Arrange
        String htmlContent = """
            <html>
            <head><meta charset='UTF-8'></head>
            <body>
                <h1>Test Resume</h1>
                <p>John Doe</p>
            </body>
            </html>
            """;

        // Act
        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        // PDF files start with %PDF
        assertEquals('%', (char) pdfBytes[0]);
        assertEquals('P', (char) pdfBytes[1]);
        assertEquals('D', (char) pdfBytes[2]);
        assertEquals('F', (char) pdfBytes[3]);
    }

    @Test
    void testGeneratePdfFromHtml_PartialHtml_Success() {
        // Arrange - HTML without full structure
        String htmlContent = "<h1>Test</h1><p>Content</p>";

        // Act
        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

        // Assert
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        // Should wrap partial HTML and still generate valid PDF
        assertEquals('%', (char) pdfBytes[0]);
    }

    @Test
    void testGeneratePdfFromHtml_EmptyContent_GeneratesPdf() {
        // Arrange
        String htmlContent = "";

        // Act - Should wrap empty content and generate minimal PDF
        byte[] pdfBytes = pdfService.generatePdfFromHtml(htmlContent);

        // Assert - Should still generate a valid PDF even if empty
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }

    @Test
    void testGeneratePdfFromHtml_NullContent_ThrowsException() {
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            pdfService.generatePdfFromHtml(null);
        });
    }

    @Test
    void testBuildResumeHtml_WithAllSections() {
        // Arrange - Add all possible sections
        sampleResume.setCareerObjective("Career objective");
        sampleResume.setProfessionalSummary("Professional summary");
        sampleResume.setEnhancedCareerObjective("Enhanced career objective");
        sampleResume.setEnhancedProfessionalSummary("Enhanced professional summary");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("Enhanced career objective")); // Should use enhanced version
        assertTrue(html.contains("Enhanced professional summary")); // Should use enhanced version
        assertFalse(html.contains("Career objective</h2><p>Career objective")); // Original should not appear
    }

    @Test
    void testBuildResumeHtml_SpecialCharacters() {
        // Arrange
        sampleResume.setFirstName("John & Jane");
        sampleResume.setCareerObjective("Looking for <strong>opportunities</strong>");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("John & Jane"));
        // HTML should be properly formed even with special characters
        assertTrue(html.contains("<html>"));
        assertTrue(html.contains("</html>"));
    }

    @Test
    void testEndToEnd_BuildAndGeneratePdf() {
        // Arrange
        sampleResume.setTemplate("modern");

        // Act
        String html = pdfService.buildResumeHtml(sampleResume);
        byte[] pdfBytes = pdfService.generatePdfFromHtml(html);

        // Assert
        assertNotNull(html);
        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 1000); // A real resume PDF should be larger
        assertEquals('%', (char) pdfBytes[0]);
        assertEquals('P', (char) pdfBytes[1]);
        assertEquals('D', (char) pdfBytes[2]);
        assertEquals('F', (char) pdfBytes[3]);
    }
}
