package com.airesumebuilder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web Controller for serving HTML pages.
 * Handles route mapping for frontend views.
 */
@Controller
public class WebController {

    /**
     * GET / or /index
     * Serve the home page
     */
    @GetMapping({"/", "/index"})
    public String home() {
        return "index";
    }

    /**
     * GET /resume-form
     * Serve the resume form page
     */
    @GetMapping("/resume-form")
    public String resumeForm() {
        return "resume-form";
    }

    /**
     * GET /preview
     * Serve the resume preview page
     */
    @GetMapping("/preview")
    public String preview() {
        return "preview";
    }
}
