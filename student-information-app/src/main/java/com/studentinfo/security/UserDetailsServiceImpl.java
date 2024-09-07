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

        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.error("No user found with email: {}", email);
            throw new UsernameNotFoundException("No user present with email: " + email);
        }
        logger.info("User found: {} with roles: {}", user.getEmail(), user.getAuthorities());
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getHashedPassword(),
                user.getAuthorities()
        );
    }

}
