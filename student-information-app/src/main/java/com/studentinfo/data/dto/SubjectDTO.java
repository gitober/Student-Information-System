package com.studentinfo.data.dto;

public class SubjectDTO {

    private Long id;
    private String name;
    private Long departmentId; // Exposing only the department ID

    // Default constructor
    public SubjectDTO() {}

    // Constructor with fields
    public SubjectDTO(Long id, String name, Long departmentId) {
        this.id = id;
        this.name = name;
        this.departmentId = departmentId;
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
    public Long getDepartmentId() {
        return departmentId;
    }
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
