package com.studentinfo.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("USER")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id") // Use the exact column name from your schema
    private Long id;

    @Column(unique = true, nullable = false) // Ensure username is unique and not null
    private String username;

    private String name;

    @Column(name = "user_type", nullable = false)
    private String userType; // or use an enum if appropriate

    // Getters and Setters for userType
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @JsonIgnore
    @Column(length = 60, name = "hashed_password") // Use the exact column name
    private String hashedPassword;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "roles")
    private Set<Role> roles;

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String phoneNumber;

    @Column(unique = true, nullable = false) // Email should be unique and not null
    private String email;

    // No-argument constructor
    public User() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Convert roles to GrantedAuthority
    private Set<GrantedAuthority> getRoleAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    // Implementations of UserDetails interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoleAuthorities();
    }

    @Override
    public String getPassword() {
        return getHashedPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Override equals and hashCode based on ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
