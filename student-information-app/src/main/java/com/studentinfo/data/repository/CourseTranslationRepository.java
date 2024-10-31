package com.studentinfo.data.repository;

import com.studentinfo.data.entity.CourseTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTranslationRepository extends JpaRepository<CourseTranslation, Long> {

    // Retrieve translations by course ID and locale
    List<CourseTranslation> findByCourse_CourseIdAndLocale(Long courseId, String locale);

    // Retrieve a specific translation by course ID, locale, and field name
    Optional<CourseTranslation> findByCourse_CourseIdAndLocaleAndFieldName(Long courseId, String locale, String fieldName);

    // Retrieve all translations for a given locale
    List<CourseTranslation> findByLocale(String locale);
}
