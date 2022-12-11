package ru.yandex.practicum.filmorate.storage.filmstorage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.FILM;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component("InMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId;
    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public Optional<Film> addFilm(Film film) {
        Film filmToMemory = Film.builder()
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .genres(film.getGenres())
                .mpa(film.getMpa())
                .id(++filmId)
                .build();
        films.put(filmToMemory.getId(), filmToMemory);
        log.info("Фильм успешно добавлен");
        return Optional.of(filmToMemory);
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновление фильма прошло успешно");
        } else {
            throw new NotFoundException(useType(FILM));
        }
        return Optional.of(film);
    }

    @Override
    public Optional<Film> findFilmById(Long id) {
        if (films.containsKey(id)) {
            log.info("Фильм успешно найден");
            return Optional.ofNullable(films.get(id));
        } else
            throw new NotFoundException(useType(FILM));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Список фильмов успешно получен");
        return new ArrayList<>(films.values());
    }
}
