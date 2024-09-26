package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "grade")
public class Grade {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "grade", nullable = false, length = 5)
    private String grade;

    @Column(name = "grading_day", nullable = false)
    private LocalDate gradingDay;

    @Column(name = "student_number", nullable = false)
    private Long studentNumber;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;

    // Getters and Setters
    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDate getGradingDay() {
        return gradingDay;
    }

    public void setGradingDay(LocalDate gradingDay) {
        this.gradingDay = gradingDay;
    }

    public Long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}