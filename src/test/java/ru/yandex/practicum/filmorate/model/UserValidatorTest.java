package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidatorTest extends ValidatorTest {

    @Test
    public void emailNullValidation() {
        User user = getCorrectFilledUser();
        user.setEmail(null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Email cannot be empty or null", errorMessage);
    }

    @Test
    public void emailEmptyValidation() {
        User user = getCorrectFilledUser();
        user.setEmail("");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Email cannot be empty or null", errorMessage);
    }

    @Test
    public void emailForCorrectValidation() {
        User user = getCorrectFilledUser();
        user.setEmail("4567890");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Email should be valid", errorMessage);
    }

    @Test
    public void loginNotBlankValidation() {
        User user = getCorrectFilledUser();
        user.setLogin("asdf dfgh");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Login must match ^[a-zA-Z0-9]+$", errorMessage);
    }

    @Test
    public void loginEmptyValidation() {
        User user = getCorrectFilledUser();
        user.setLogin("");

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Login must match ^[a-zA-Z0-9]+$", errorMessage);
    }

    @Test
    public void loginNullValidation() {
        User user = getCorrectFilledUser();
        user.setLogin(null);

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Login cannot be null", errorMessage);
    }

    @Test
    public void birthdayPastValidation() {
        User user = getCorrectFilledUser();
        user.setBirthday(LocalDate.of(2025, 1, 1));

        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        assertEquals(1, constraintViolations.size());

        String errorMessage = constraintViolations.iterator().next().getMessage();
        assertEquals("Your birthday should be in past", errorMessage);
    }

    private User getCorrectFilledUser() {
        User user = new User();
        user.setLogin("12345");
        user.setEmail("12345@gmail.com");
        user.setName("Lalala");
        user.setBirthday(LocalDate.of(2010, Month.AUGUST, 1));
        return user;
    }
}