package com.studentinfo.data.dto;

import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    // Getters and Setters
    @Setter
    @Getter
    private Long id;
    @Setter
    @Getter
    private String username;
    @Setter
    @Getter
    private String name;
    private String email; // Add if needed for updates
    private String phoneNumber; // Add if needed for updates
    @Setter
    @Getter
    private Set<String> roles;

}
