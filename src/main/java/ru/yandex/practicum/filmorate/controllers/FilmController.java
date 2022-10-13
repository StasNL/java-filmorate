package ru.yandex.practicum.filmorate.controllers;
import lombok.Getter;
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
@Getter
public class FilmController {

    private int filmId;
    private HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        Film filmToMemory = film.create(++filmId);
        films.put(filmToMemory.getId(), filmToMemory);
        log.info("Фильм успешно добавлен");
        return filmToMemory;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film filmToMemory;
        if(films.containsKey(film.getId())) {
            filmToMemory = films.get(filmId).update(film);
            log.info("Обновление фильма прошло успешно");
        }
        else {
            throw new RuntimeException("Фильм отсутствует в библиотеке.");
        }
        return filmToMemory;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
