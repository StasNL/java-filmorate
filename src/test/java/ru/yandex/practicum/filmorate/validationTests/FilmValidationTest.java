package ru.yandex.practicum.filmorate.validationTests;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.*;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTest {

    static FilmController filmController;

    @BeforeEach
    void createFilmController() {
        filmController = new FilmController();
    }

//    Суть тестов состоит в том, чтобы отлавливать ошибку валидации и считывать сообщение,
//    в котором описано поле, содержащее ошибку

    @Test
    void shouldntCreateFilmWithEmptyName() {
        Film film = Film.builder()
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(1985,12,1))
                .duration(90)
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(film));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Name should contain at least one character.", errorMessage);
    }

    @Test
    void shouldntCreateFilmWithTooBidDescription() {
         String randomDescription = RandomStringUtils.randomAlphabetic(201);
        Film film = Film.builder()
                .name("Titanic")
                .description(randomDescription)
                .releaseDate(LocalDate.of(1985,12,1))
                .duration(90)
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(film));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Max length of description is 200 symbols.", errorMessage);
    }

    @Test
    void shouldntCreateFilmWithReleaseDateBefore1895() {
        Film film = Film.builder()
                .name("Correct name")
                .description("Best movie")
                .releaseDate(LocalDate.of(985,12,1))
                .duration(90)
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(film));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Release date should be after 28.12.1895.", errorMessage);
    }

    @Test
    void shouldntCreateFilmWithNegativeDuration() {
        Film film = Film.builder()
                .name("Correct name")
                .description("Best movie")
                .releaseDate(LocalDate.of(1985,12,1))
                .duration(-90)
                .build();
        Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(film));
        String errorMessage = thrown.getMessage().split(":")[1].trim();
        assertEquals("Duration should be positive natural digit.", errorMessage);
    }

    @Test
    void shouldCreateFilmWithCorrectFields() {
        Film film = Film.builder()
                .name("Correct name")
                .description("Best movie")
                .releaseDate(LocalDate.of(1985,12,1))
                .duration(90)
                .build();

        String errorMessage = "Ошибка";
//     Ловим ошибку, которая выбрасывается при отсутствии ошибки валидации
        try {
            Throwable thrown = assertThrows(ConstraintViolationException.class, () -> validate(film));
        } catch (AssertionFailedError e) {
            errorMessage = "Ошибки нет";
        }
        assertEquals("Ошибки нет", errorMessage);
    }

    public void validate(Film film) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
