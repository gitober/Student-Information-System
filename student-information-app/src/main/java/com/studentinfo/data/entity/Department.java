package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
public class Department {

    // Getters and Setters
    // Fields
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name")
    private String name;

    @Setter
    @Getter
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers;

    @Setter
    @Getter
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subject> subjects;

    // Constructors
    public Department() {}

    public Department(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return name;
    }

    public void setDepartmentName(String name) {
        this.name = name;
    }

}
