package com.studentinfo.data.repository;

import com.studentinfo.data.entity.SubjectTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTranslationRepository extends JpaRepository<SubjectTranslation, Long> {
    List<SubjectTranslation> findBySubjectIdAndLocale(Long subjectId, String locale); // Make sure this method exists
    List<SubjectTranslation> findByLocale(String locale);
}
