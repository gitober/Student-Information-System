package com.studentinfo.data.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STUDENT")  // This value will be stored in the `user_type` column for students
public class Student extends User {

    private String grade;
    private String studentClass;

    // Getters and setters
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
}