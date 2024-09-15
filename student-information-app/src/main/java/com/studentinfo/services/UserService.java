package com.studentinfo.services;

import com.studentinfo.data.dto.EditProfileDTO;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }


    // Check if email is already taken by another user
    public boolean isEmailTaken(String email, Long userId) {
        User existingUser = userRepository.findByEmail(email);
        return existingUser != null && !existingUser.getId().equals(userId);
    }

    // Authenticate user based on username and password
    public Optional<User> authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.debug("Provided password: {}", password);
            logger.debug("Stored password hash: {}", user.getHashedPassword());
        }
        return Optional.ofNullable(user);
    }


    // List all users
    public List<User> list() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    // Save or update a user (handles both cases)
    public User save(User user) {
        if (user.getId() != null) {
            Optional<User> existingUserOptional = userRepository.findById(user.getId());
            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();
                existingUser.setFirstName(user.getFirstName());
                existingUser.setLastName(user.getLastName());
                existingUser.setEmail(user.getEmail());
                existingUser.setPhoneNumber(user.getPhoneNumber());

                if (user.getHashedPassword() != null && !user.getHashedPassword().isEmpty()) {
                    existingUser.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
                }

                return userRepository.save(existingUser);
            }
        }
        user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
        return userRepository.save(user);
    }



    // Delete a user by ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Update user profile using EditProfileDTO
    @Transactional
    public Optional<User> updateUserProfile(EditProfileDTO editProfileDTO) {
        return userRepository.findById(editProfileDTO.getId())
                .map(existingUser -> {
                    // Validate new email uniqueness
                    if (isEmailTaken(editProfileDTO.getEmail(), editProfileDTO.getId())) {
                        throw new IllegalArgumentException("Email is already taken.");
                    }

                    existingUser.setFirstName(editProfileDTO.getFirstName());
                    existingUser.setLastName(editProfileDTO.getLastName());
                    existingUser.setEmail(editProfileDTO.getEmail());
                    existingUser.setPhoneNumber(editProfileDTO.getPhoneNumber());

                    // Update password only if it's provided and not empty
                    if (editProfileDTO.getPassword() != null && !editProfileDTO.getPassword().isEmpty()) {
                        existingUser.setHashedPassword(passwordEncoder.encode(editProfileDTO.getPassword()));
                    }

                    return userRepository.save(existingUser);
                });
    }

    // Helper method to get the current HttpServletRequest
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No current request available", e);
        }
    }

    // Helper method to get the current HttpServletResponse
    private HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            if (response == null) {
                throw new IllegalStateException("No response available");
            }
            return response;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No current response available", e);
        }
    }

    // Find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Encode password
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
