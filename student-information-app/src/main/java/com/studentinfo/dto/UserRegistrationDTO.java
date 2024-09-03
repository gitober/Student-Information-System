package com.studentinfo.dto;

public class UserRegistrationDTO {

    private String username;
    private String password;
    private String role;

    // Default constructor
    public UserRegistrationDTO() {
    }

    // Parameterized constructor
    public UserRegistrationDTO(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRegistrationDTO{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
