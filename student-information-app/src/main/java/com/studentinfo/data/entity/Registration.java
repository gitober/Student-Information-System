package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registration")
public class Registration {

    // Fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @Column(name = "registration_day")
    private LocalDate registrationDay;

    @Column(name = "course_payment")
    private double coursePayment;

    @Column(name = "student_number")
    private Long studentNumber;

    @Column(name = "batch_id", nullable = true)
    private Long batchId;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "student_number", referencedColumnName = "student_number", insertable = false, updatable = false)
    private Student student;

    // Getters and Setters
    public Long getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId) {
        this.registrationId = registrationId;
    }

    public LocalDate getRegistrationDay() {
        return registrationDay;
    }

    public void setRegistrationDay(LocalDate registrationDay) {
        this.registrationDay = registrationDay;
    }

    public double getCoursePayment() {
        return coursePayment;
    }

    public void setCoursePayment(double coursePayment) {
        this.coursePayment = coursePayment;
    }

    public Long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
