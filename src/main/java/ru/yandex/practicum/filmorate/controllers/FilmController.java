package ru.yandex.practicum.filmorate.controllers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private int filmId;
    private HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Film filmToMemory = Film.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .id(++filmId)
                .build();
        films.put(filmToMemory.getId(), filmToMemory);
        log.info("Фильм успешно добавлен");
        return filmToMemory;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновление фильма прошло успешно");
        }
        else {
            throw new RuntimeException("Фильм отсутствует в библиотеке.");
        }
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
