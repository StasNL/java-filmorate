package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotations.DateAfter;

import javax.validation.constraints.*;

@Data
@Builder
public class Film {
    public final static String PATTERN_FOR_TIME = "dd.MM.yyyy";

    private int id;

    @NonNull
    @NotBlank(message = "Name should contain at least one character.")
    private String name;

    @NonNull
    @Size(max = 200, message = "Max length of description is 200 symbols.")
    private String description;

    @NonNull
    @DateAfter(date = "28.12.1895", message = "Release date should be after 28.12.1895.")
    private LocalDate releaseDate;

    @NonNull
    @Positive(message = "Duration should be positive natural digit.")
    private int duration;

    public void setReleaseDate(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(PATTERN_FOR_TIME);
        this.releaseDate = LocalDate.parse(date, dtf);
    }

    public Film update(Film film) {
        this.name = film.name;
        this.description = film.description;
        this.releaseDate = film.releaseDate;
        this.duration = film.duration;
        return this;
    }

    public Film create(int filmId) {
        this.id = filmId;
        return this;
    }
}
