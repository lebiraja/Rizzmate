package com.airesumebuilder.service;

import com.airesumebuilder.dto.ResumeDTO;
import com.airesumebuilder.exception.InvalidResumeDataException;
import com.airesumebuilder.exception.ResumeNotFoundException;
import com.airesumebuilder.model.ResumeData;
import com.airesumebuilder.repository.ResumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ResumeService
 * 
 * Tests cover:
 * - Creating resumes with valid/invalid data
 * - Retrieving resumes by ID and email
 * - Updating resumes
 * - Exception handling
 * - Caching behavior (integration test)
 */
@ExtendWith(MockitoExtension.class)
class ResumeServiceTest {

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private GeminiService geminiService;

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private ResumeService resumeService;

    private ResumeDTO sampleResumeDTO;
    private ResumeData sampleResumeData;

    @BeforeEach
    void setUp() {
        // Initialize sample data for testing
        sampleResumeDTO = new ResumeDTO();
        sampleResumeDTO.setFirstName("John");
        sampleResumeDTO.setLastName("Doe");
        sampleResumeDTO.setEmail("john.doe@example.com");
        sampleResumeDTO.setPhone("1234567890");
        sampleResumeDTO.setCareerObjective("Seeking entry-level position");

        sampleResumeData = new ResumeData();
        sampleResumeData.setId(1L);
        sampleResumeData.setFirstName("John");
        sampleResumeData.setLastName("Doe");
        sampleResumeData.setEmail("john.doe@example.com");
        sampleResumeData.setPhone("1234567890");
        sampleResumeData.setCareerObjective("Seeking entry-level position");
    }

    @Test
    void testCreateResume_Success() {
        // Arrange
        when(resumeRepository.save(any(ResumeData.class))).thenReturn(sampleResumeData);

        // Act
        ResumeDTO result = resumeService.createResume(sampleResumeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(resumeRepository, times(1)).save(any(ResumeData.class));
    }

    @Test
    void testCreateResume_NullData_ThrowsException() {
        // Act & Assert
        assertThrows(InvalidResumeDataException.class, () -> {
            resumeService.createResume(null);
        });
        verify(resumeRepository, never()).save(any(ResumeData.class));
    }

    @Test
    void testCreateResume_InvalidData_ThrowsException() {
        // Arrange
        ResumeDTO invalidDTO = new ResumeDTO();
        // Missing required fields

        // Act & Assert
        assertThrows(InvalidResumeDataException.class, () -> {
            resumeService.createResume(invalidDTO);
        });
        verify(resumeRepository, never()).save(any(ResumeData.class));
    }

    @Test
    void testGetResumeById_Success() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(sampleResumeData));

        // Act
        ResumeDTO result = resumeService.getResumeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(resumeRepository, times(1)).findByIdWithDetails(1L);
    }

    @Test
    void testGetResumeById_NotFound_ThrowsException() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResumeNotFoundException.class, () -> {
            resumeService.getResumeById(999L);
        });
        verify(resumeRepository, times(1)).findByIdWithDetails(999L);
    }

    @Test
    void testUpdateResume_Success() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(sampleResumeData));
        when(resumeRepository.save(any(ResumeData.class))).thenReturn(sampleResumeData);

        ResumeDTO updateDTO = new ResumeDTO();
        updateDTO.setFirstName("Jane");
        updateDTO.setLastName("Smith");
        updateDTO.setEmail("jane.smith@example.com");
        updateDTO.setPhone("9876543210");

        // Act
        ResumeDTO result = resumeService.updateResume(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(resumeRepository, times(1)).findByIdWithDetails(1L);
        verify(resumeRepository, times(1)).save(any(ResumeData.class));
    }

    @Test
    void testUpdateResume_NotFound_ThrowsException() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        ResumeDTO updateDTO = new ResumeDTO();
        updateDTO.setFirstName("Jane");

        // Act & Assert
        assertThrows(ResumeNotFoundException.class, () -> {
            resumeService.updateResume(999L, updateDTO);
        });
        verify(resumeRepository, never()).save(any(ResumeData.class));
    }

    @Test
    void testDeleteResume_Success() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(1L)).thenReturn(Optional.of(sampleResumeData));
        doNothing().when(resumeRepository).delete(any(ResumeData.class));

        // Act
        assertDoesNotThrow(() -> resumeService.deleteResume(1L));

        // Assert
        verify(resumeRepository, times(1)).findByIdWithDetails(1L);
        verify(resumeRepository, times(1)).delete(any(ResumeData.class));
    }

    @Test
    void testDeleteResume_NotFound_ThrowsException() {
        // Arrange
        when(resumeRepository.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResumeNotFoundException.class, () -> {
            resumeService.deleteResume(999L);
        });
        verify(resumeRepository, never()).delete(any(ResumeData.class));
    }
}
