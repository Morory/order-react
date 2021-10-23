package com.management.order.repository;

import com.management.order.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final User user = User.builder()
            .username("testName")
            .email("testEmail@email.com")
            .password("testPassword")
            .build();

    @Test
    void save() {
        User savedUser = userRepository.save(user);
        assertEquals(user.getUsername(), savedUser.getUsername());
    }

    @Test
    void findByUsername() {
        User savedUser = userRepository.save(user);
        Optional<User> optionalFoundUser = userRepository.findByUsername(user.getUsername());
        if(!optionalFoundUser.isPresent())
            assertThrows(NullPointerException.class, () -> {});
        User foundUser = optionalFoundUser.get();
        assertEquals(savedUser.getUsername(), foundUser.getUsername());
    }

    @Test
    void existsByUsername() {
        userRepository.save(user);
        assertTrue(userRepository.existsByUsername(user.getUsername()));
    }

    @Test
    void existsByEmail() {
        userRepository.save(user);
        assertTrue(userRepository.existsByEmail(user.getEmail()));
    }

    @Test
    void update() {
        String updateUserName = "updatedUserName";

        User originalUser = userRepository.save(user);
        originalUser.setUsername(updateUserName);

        User updatedUser = userRepository.save(originalUser);
        assertEquals(updateUserName, updatedUser.getUsername());
    }

    @Test
    void delete() {
        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());
        assertFalse(userRepository.findById(savedUser.getId()).isPresent());
    }
}
