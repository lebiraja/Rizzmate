package com.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a user's education details.
 * Maps to the education table in the database.
 */
@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String university;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private Integer graduationYear;

    @Column
    private Double cgpa;

    @Column(length = 500)
    private String achievements;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore  // Prevent infinite recursion during JSON serialization
    private ResumeData resume;

    // Constructors
    public Education() {
    }

    public Education(Long id, String degree, String university, String fieldOfStudy, 
                     Integer graduationYear, Double cgpa, String achievements, ResumeData resume) {
        this.id = id;
        this.degree = degree;
        this.university = university;
        this.fieldOfStudy = fieldOfStudy;
        this.graduationYear = graduationYear;
        this.cgpa = cgpa;
        this.achievements = achievements;
        this.resume = resume;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public ResumeData getResume() {
        return resume;
    }

    public void setResume(ResumeData resume) {
        this.resume = resume;
    }
}
