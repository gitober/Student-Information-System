package com.studentinfo.data.dto;

import com.studentinfo.data.entity.Language;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationDTO {
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phoneNumber;
    private String email;
    private String password;
    private String role;
    private Language currentLocale;
}
