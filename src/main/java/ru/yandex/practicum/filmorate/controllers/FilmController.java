package ru.yandex.practicum.filmorate.controllers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping()
@Slf4j
public class FilmController {

    FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Optional<Film> addFilm(@Valid @RequestBody Film film) {
        return filmService.add(film);
    }

    @PutMapping("/films")
    public Optional<Film> updateFilm(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAll();
    }

    @GetMapping("/films/{id}")
    public Optional<Film> getFilmById(@Valid @PathVariable Long id) {
        return filmService.findFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@Valid @PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@Valid @PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@Valid @RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        return filmService.getAllMPA();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById (@PathVariable Integer id) {
        return filmService.getMPAById(id);
    }
}
