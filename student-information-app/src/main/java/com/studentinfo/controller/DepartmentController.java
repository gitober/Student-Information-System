package com.studentinfo.controller;

import com.studentinfo.data.entity.DepartmentTranslation;
import com.studentinfo.services.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final TranslationService translationService;

    @Autowired
    public DepartmentController(TranslationService translationService) {
        this.translationService = translationService;
    }

    // Get translations for departments in a specific language
    @GetMapping("/translations")
    public ResponseEntity<List<DepartmentTranslation>> getDepartmentTranslations(@RequestParam String locale) {
        List<DepartmentTranslation> translations = translationService.getDepartmentTranslationsByLocale(locale);
        return ResponseEntity.ok(translations); // Return 200 OK with the list of translated departments
    }
}
