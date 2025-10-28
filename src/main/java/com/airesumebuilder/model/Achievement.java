package com.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

/**
 * Entity class representing a user's achievement.
 * Maps to the achievement table in the database.
 */
@Entity
@Table(name = "achievement")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String achievementTitle;

    @Column(length = 1000)
    private String description;

    @Column
    private String date;

    @Column
    private String category;

    @ManyToOne
    @JoinColumn(name = "resume_id")
    @JsonIgnore  // Prevent infinite recursion during JSON serialization
    private ResumeData resume;

    // Constructors
    public Achievement() {
    }

    public Achievement(Long id, String achievementTitle, String description, String date, 
                       String category, ResumeData resume) {
        this.id = id;
        this.achievementTitle = achievementTitle;
        this.description = description;
        this.date = date;
        this.category = category;
        this.resume = resume;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAchievementTitle() {
        return achievementTitle;
    }

    public void setAchievementTitle(String achievementTitle) {
        this.achievementTitle = achievementTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ResumeData getResume() {
        return resume;
    }

    public void setResume(ResumeData resume) {
        this.resume = resume;
    }
}
