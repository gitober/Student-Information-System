package com.studentinfo.services;

import com.studentinfo.data.User;
import com.studentinfo.data.Role;
import com.studentinfo.repository.UserRepository;
import com.studentinfo.dto.UserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Existing CRUD operations

    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Page<User> list(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> list(Pageable pageable, Specification<User> filter) {
        return userRepository.findAll(filter, pageable);
    }

    public int count() {
        return (int) userRepository.count();
    }

    // New methods for registration and authentication

    public User registerUser(UserRegistrationDTO userRegistrationDTO) {
        // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(userRegistrationDTO.getPassword());

        // Create a new User entity from the DTO
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setHashedPassword(encryptedPassword);  // Use hashedPassword instead of plain password

        // Convert the role string from DTO to a Set<Role>
        Set<Role> roles = new HashSet<>();
        try {
            Role role = Role.valueOf(userRegistrationDTO.getRole()); // Convert role string to Role enum
            roles.add(role);
        } catch (IllegalArgumentException e) {
            // Handle invalid role (e.g., log an error or throw an exception)
            throw new IllegalArgumentException("Invalid role: " + userRegistrationDTO.getRole());
        }
        user.setRoles(roles);

        // Save the user in the database
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean authenticateUser(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return passwordEncoder.matches(rawPassword, user.getHashedPassword());
        }
        return false;
    }
}
