package com.studentinfo.data.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id") // Specify the column name
    private Long departmentId; // Rename this field

    @Column(name = "name") // Specify the column name if needed
    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subject> subjects;

    // Constructors
    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getDepartmentId() {
        return departmentId; // Update this to match the renamed field
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId; // Update this to match the renamed field
    }

    public String getDepartmentName() {
        return name;
    }

    public void setDepartmentName(String name) {
        this.name = name;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
