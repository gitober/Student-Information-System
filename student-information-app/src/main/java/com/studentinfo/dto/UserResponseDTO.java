package com.studentinfo.dto;

public class UserResponseDTO {

    private String username;
    private String role;

    // Default constructor
    public UserResponseDTO() {
    }

    // Parameterized constructor
    public UserResponseDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
