package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Language;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.entity.UserTranslation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
public class UserTranslationRepositoryTest {

    @Autowired
    private UserTranslationRepository userTranslationRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create and save a test user with a userType
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@example.com");
        testUser.setUserType("USER"); // Ensure this matches your userType field requirements
        userRepository.saveAndFlush(testUser);

        // Create and save UserTranslations for the test user
        UserTranslation translation1 = new UserTranslation();
        translation1.setUser(testUser);
        translation1.setLocale(Language.EN);
        translation1.setFieldName("firstName");
        translation1.setTranslatedValue("Test");
        userTranslationRepository.saveAndFlush(translation1);

        UserTranslation translation2 = new UserTranslation();
        translation2.setUser(testUser);
        translation2.setLocale(Language.FI);
        translation2.setFieldName("lastName");
        translation2.setTranslatedValue("User");
        userTranslationRepository.saveAndFlush(translation2);
    }

    @AfterEach
    public void tearDown() {
        userTranslationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testFindByUserIdAndLocale() {
        List<UserTranslation> translations = userTranslationRepository.findByUser_IdAndLocale(testUser.getId(), Language.EN);
        assertThat(translations).hasSize(1);
        assertThat(translations.get(0).getTranslatedValue()).isEqualTo("Test");
    }

    @Test
    public void testFindByLocale() {
        List<UserTranslation> translations = userTranslationRepository.findByLocale(Language.FI);
        assertThat(translations).hasSize(1);
        assertThat(translations.get(0).getTranslatedValue()).isEqualTo("User");
    }
}
