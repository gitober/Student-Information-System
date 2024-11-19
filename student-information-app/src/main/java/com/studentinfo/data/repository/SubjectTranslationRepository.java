package com.studentinfo.data.repository;

import com.studentinfo.data.entity.SubjectTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTranslationRepository extends JpaRepository<SubjectTranslation, Long> {

    // Retrieve translations by subject ID and locale
    List<SubjectTranslation> findBySubjectIdAndLocale(Long subjectId, String locale);

    // Retrieve a specific translation by subject ID, locale, and field name
    List<SubjectTranslation> findByLocale(String locale);
}
