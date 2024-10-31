package com.studentinfo.data.repository;

import com.studentinfo.data.entity.DepartmentTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentTranslationRepository extends JpaRepository<DepartmentTranslation, Long> {
    List<DepartmentTranslation> findByDepartmentIdAndLocale(Long departmentId, String locale); // Ensure this method is present
    List<DepartmentTranslation> findByLocale(String locale);
}
