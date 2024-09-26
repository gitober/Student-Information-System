package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    // Find a teacher by username
    Optional<Teacher> findByUsername(String username);

    // Find teachers by department name
    List<Teacher> findByDepartment_Name(String departmentName);

    // Find teachers by subject name
    List<Teacher> findBySubject_Name(String subjectName);

    // Find teachers by both department name and subject name
    List<Teacher> findByDepartment_NameAndSubject_Name(String departmentName, String subjectName);

    // Find teachers and fetch their courses eagerly
    @Query("SELECT t FROM Teacher t JOIN FETCH t.courses WHERE t.id = :id")
    Teacher findTeacherWithCourses(Long id);

    // Fetch teacher with courses eagerly
    @Query("SELECT t FROM Teacher t LEFT JOIN FETCH t.courses WHERE t.username = :username")
    Optional<Teacher> findTeacherByUsernameWithCourses(@Param("username") String username);

}
