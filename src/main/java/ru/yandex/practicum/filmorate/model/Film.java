package ru.yandex.practicum.filmorate.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.annotations.DateAfter;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
