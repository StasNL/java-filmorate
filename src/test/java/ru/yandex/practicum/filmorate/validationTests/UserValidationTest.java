package ru.yandex.practicum.filmorate.validationTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {

//    Суть тестов состоит в том, чтобы отлавливать ошибку валидации и считывать сообщение,
//    в котором описано поле, содержащее ошибку

    @Test
    void shouldntCreateUserWithIncorrectFormatEmail() {
        User user = User.builder()
                .email("mailmail.ru")
                .name("Vasya")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("Login")
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("email should contain @", errorMessage);
    }

    @Test
    void shouldntCreateUserWithEmptyEmail() {
        User user = User.builder()
                .email("")
                .name("Vasya")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("Login")
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("email shouldn't be empty", errorMessage);
    }

    @Test
    void shouldntCreateUserWithEmptyLogin() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Vasya")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("")
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Login shouldn't be empty", errorMessage);
    }

    @Test
    void shouldntCreateUserWithLoginWithSpace() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Vasya")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("log in")
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Login shouldn't contain spaces", errorMessage);
    }

    @Test
    void shouldntCreateUserWithBirthdayInFuture() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Vasya")
                .birthday(LocalDate.of(2985, 12, 1))
                .login("login")
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Birthday shouldn't be in the future", errorMessage);
    }

    @Test
    void shouldCreateUserWithCorrectFields() {
        User user = User.builder()
                .email("mail@mail.ru")
                .name("Vasya")
                .birthday(LocalDate.of(1985, 12, 1))
                .login("login")
                .build();

        String errorMessage = "Ошибка";
//     Ловим ошибку, которая выбрасывается при отсутствии ошибки валидации
        try {
            Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(user));
        } catch (AssertionFailedError e) {
            errorMessage = "Ошибки нет";
        }
        assertEquals("Ошибки нет", errorMessage);
    }

    public void validate(User user) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
