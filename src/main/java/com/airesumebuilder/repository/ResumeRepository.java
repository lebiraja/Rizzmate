package com.airesumebuilder.repository;

import com.airesumebuilder.model.ResumeData;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository interface for ResumeData entity.
 * Provides CRUD operations and custom queries for resume data.
 * Uses @EntityGraph to optimize queries and prevent N+1 problem.
 */
@Repository
public interface ResumeRepository extends JpaRepository<ResumeData, Long> {
    
    /**
     * Find resume by ID with all relationships eagerly loaded
     * This prevents N+1 query problem
     */
    @EntityGraph(attributePaths = {
        "educations", "projects", "skills", 
        "certifications", "languages", "achievements"
    })
    @Query("SELECT r FROM ResumeData r WHERE r.id = :id")
    Optional<ResumeData> findByIdWithDetails(@Param("id") Long id);
    
    /**
     * Find resume by email
     */
    @EntityGraph(attributePaths = {
        "educations", "projects", "skills", 
        "certifications", "languages", "achievements"
    })
    Optional<ResumeData> findByEmail(String email);

    /**
     * Find resume by first and last name
     */
    Optional<ResumeData> findByFirstNameAndLastName(String firstName, String lastName);
}
