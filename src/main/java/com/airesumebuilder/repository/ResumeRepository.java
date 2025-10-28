package com.airesumebuilder.repository;

import com.airesumebuilder.model.ResumeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for ResumeData entity.
 * Provides CRUD operations and custom queries for resume data.
 */
@Repository
public interface ResumeRepository extends JpaRepository<ResumeData, Long> {
    
    /**
     * Find resume by email
     */
    Optional<ResumeData> findByEmail(String email);

    /**
     * Find resume by first and last name
     */
    Optional<ResumeData> findByFirstNameAndLastName(String firstName, String lastName);
}
