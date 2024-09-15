package com.studentinfo.data.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TEACHER")  // This value will be stored in the `user_type` column for teachers
public class Teacher extends User {

    private String subject;
    private String department;

    // Getters and setters
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}