package com.studentinfo.data.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {

    private String address;
    private String grade;
    private String studentClass;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "registration",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), // Use user_id as the primary key
            inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    )
    private Set<Course> courses = new HashSet<>();
}
