package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @Column(name = "registration_day")
    private LocalDate registrationDay;

    @Column(name = "course_payment")
    private Double coursePayment; // Allow null values

    @Column(name = "student_number")
    private Long studentNumber; // Added studentNumber field

    @ManyToOne
    @JoinColumn(name = "student_number", referencedColumnName = "student_number", insertable = false, updatable = false)
    private Student student;

    @Column(name = "batch_id", nullable = true)
    private Long batchId;

    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

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

    public Double getCoursePayment() {
        return coursePayment;
    }

    public void setCoursePayment(Double coursePayment) {
        this.coursePayment = coursePayment;
    }

    public Long getStudentNumber() {
        return studentNumber; // Corrected getter for studentNumber
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber; // Corrected setter for studentNumber
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
