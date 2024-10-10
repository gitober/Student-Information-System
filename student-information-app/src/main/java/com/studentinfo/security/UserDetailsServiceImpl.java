package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Attempting to find user by email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);

        User user = userOpt.orElseThrow(() -> {
            logger.error("No user found with email: {}", email);
            return new UsernameNotFoundException("No user present with email: " + email);
        });

        logger.info("User found: {} with roles: {}", user.getEmail(), user.getAuthorities());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getHashedPassword(),
                user.getAuthorities()
        );
    }
}