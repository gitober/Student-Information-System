package com.studentinfo.data.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    private String address;
    private String grade;
    private String studentClass;

    @Column(name = "student_number", unique = true)
    private Long studentNumber;

    // Getters and setters
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public Long getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Long studentNumber) {
        this.studentNumber = studentNumber;
    }
}
