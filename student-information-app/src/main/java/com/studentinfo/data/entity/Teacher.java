package com.studentinfo.data.entity;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TEACHER")  // This value will be stored in the `user_type` column for teachers
public class Teacher extends User {

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = true)
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    // Getters and setters
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
