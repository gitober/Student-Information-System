package com.studentinfo.data.repository;

import com.studentinfo.data.entity.DepartmentTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentTranslationRepository extends JpaRepository<DepartmentTranslation, Long> {

    // Retrieve translations by department ID and locale
    List<DepartmentTranslation> findByDepartmentIdAndLocale(Long departmentId, String locale);

    // Retrieve a specific translation by department ID, locale, and field name
    List<DepartmentTranslation> findByLocale(String locale);
}
