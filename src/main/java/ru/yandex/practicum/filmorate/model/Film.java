package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.annotations.DateAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Film {

    @NonNull
    @PositiveOrZero
    private long id;

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
    private Integer duration;

    private Set<Genre> genres;

    private Mpa mpa;

    private Set<Long> likes = new HashSet<>();
}
