package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "grade")
public class Grade {

    // Getters and Setters
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Integer gradeId;

    @Column(name = "grade", nullable = false, length = 5)
    private String gradeValue;

    @Column(name = "grading_day", nullable = false)
    private LocalDate gradingDay;

    @Column(name = "student_number", nullable = false)
    private Long studentNumber;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;

}
