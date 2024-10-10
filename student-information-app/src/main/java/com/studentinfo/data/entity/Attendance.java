package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "attendance")
public class Attendance {

    // Getters and Setters
    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column(name = "attendance_status", nullable = false)
    private String attendanceStatus;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_number", referencedColumnName = "student_number")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;

    // Constructors
    // Default constructor (required by JPA)
    public Attendance() {}

    // Constructor with fields (excluding attendanceId)
    public Attendance(String attendanceStatus, LocalDate attendanceDate, Student student, Course course) {
        this.attendanceStatus = attendanceStatus;
        this.attendanceDate = attendanceDate;
        this.student = student;
        this.course = course;
    }

}
