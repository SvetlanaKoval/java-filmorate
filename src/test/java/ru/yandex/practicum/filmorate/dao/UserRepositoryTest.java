package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.BaseIntegrationTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAll() {
        List<User> users = userRepository.findAll();

        assertThat(users)
            .isNotNull()
            .isNotEmpty()
            .allSatisfy(user -> assertThat(user).hasNoNullFieldsOrProperties());
    }

    @Test
    void testGetById_UserExists() {
        User user = userRepository.getById(1L);

        assertThat(user)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    void testGetById_UserDoesNotExist() {
        assertThatThrownBy(() -> userRepository.getById(999L))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Пользователь  с ID: 999 не найден");
    }

    @Test
    void testFindAllByIds() {
        Set<Long> ids = Set.of(1L, 2L);
        List<User> users = userRepository.findAllByIds(ids);

        assertThat(users)
            .isNotNull()
            .hasSize(2)
            .allSatisfy(user -> assertThat(ids).contains(user.getId()));
    }

    @Test
    void testFindAllByIds_EmptySet() {
        List<User> users = userRepository.findAllByIds(Collections.emptySet());

        assertThat(users).isEmpty();
    }

    @Test
    void testFindByEmail_UserExists() {
        Optional<User> userOptional = userRepository.findByEmail("user2@example.com");

        assertThat(userOptional)
            .isPresent()
            .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("email", "user2@example.com"));
    }

    @Test
    void testFindByEmail_UserDoesNotExist() {
        Optional<User> userOptional = userRepository.findByEmail("nonexistent@example.com");

        assertThat(userOptional).isNotPresent();
    }

    @Test
    void testSave() {
        User user = new User();
        user.setEmail("newuser@example.com");
        user.setLogin("newuser");
        user.setName("New User");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User savedUser = userRepository.save(user);

        assertThat(savedUser)
            .isNotNull()
            .hasFieldOrPropertyWithValue("email", "newuser@example.com")
            .hasFieldOrPropertyWithValue("login", "newuser")
            .hasFieldOrPropertyWithValue("name", "New User")
            .hasFieldOrPropertyWithValue("birthday", LocalDate.of(1990, 1, 1))
            .hasNoNullFieldsOrPropertiesExcept("id");

        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void testUpdate() {
        User user = userRepository.getById(1L);
        user.setName("Updated Name");

        User updatedUser = userRepository.update(user);

        assertThat(updatedUser)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("name", "Updated Name");
    }

    @Test
    void testDelete_UserExists() {
        boolean deleted = userRepository.delete(1L);

        assertThat(deleted).isTrue();
    }

    @Test
    void testDelete_UserDoesNotExist() {
        boolean deleted = userRepository.delete(999L);

        assertThat(deleted).isFalse();
    }

}