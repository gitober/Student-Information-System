package com.studentinfo.data.dto;

import java.util.Set;

public class DepartmentDTO {

    private Long id;
    private String name;
    private Set<Long> teacherIds; // Assuming you want to expose only teacher IDs
    private Set<Long> subjectIds; // Assuming you want to expose only subject IDs

    // Default constructor
    public DepartmentDTO() {}

    // Constructor with fields
    public DepartmentDTO(Long id, String name, Set<Long> teacherIds, Set<Long> subjectIds) {
        this.id = id;
        this.name = name;
        this.teacherIds = teacherIds;
        this.subjectIds = subjectIds;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(Set<Long> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public Set<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(Set<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }
}
