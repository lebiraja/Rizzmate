package com.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a user's certification.
 * Maps to the certification table in the database.
 */
@Entity
@Table(name = "certification")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String certificationName;

    @Column
    private String issuer;

    @Column
    private String issueDate;

    @Column
    private String expiryDate;

    @Column(length = 500)
    private String description;

    @Column
    private String certificateLink;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore  // Prevent infinite recursion during JSON serialization
    private ResumeData resume;

    // Constructors
    public Certification() {
    }

    public Certification(Long id, String certificationName, String issuer, String issueDate,
                         String expiryDate, String description, String certificateLink, ResumeData resume) {
        this.id = id;
        this.certificationName = certificationName;
        this.issuer = issuer;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.description = description;
        this.certificateLink = certificateLink;
        this.resume = resume;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCertificateLink() {
        return certificateLink;
    }

    public void setCertificateLink(String certificateLink) {
        this.certificateLink = certificateLink;
    }

    public ResumeData getResume() {
        return resume;
    }

    public void setResume(ResumeData resume) {
        this.resume = resume;
    }
}
