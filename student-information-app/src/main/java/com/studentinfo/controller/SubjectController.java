package com.studentinfo.controller;

import com.studentinfo.data.entity.SubjectTranslation;
import com.studentinfo.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final TranslationService translationService;

    @Autowired
    public SubjectController(TranslationService translationService) {
        this.translationService = translationService;
    }

    // Get translations for a specific subject and locale
    @GetMapping("/{id}/translations")
    public ResponseEntity<List<SubjectTranslation>> getSubjectTranslations(@PathVariable Long id, @RequestParam String locale) {
        List<SubjectTranslation> translations = translationService.getSubjectTranslations(id, locale);
        if (translations.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if no translations found
        }
        return ResponseEntity.ok(translations); // Return 200 OK with the translations
    }

    // Additional subject-related endpoints (CRUD, etc.) can go here
}
