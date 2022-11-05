package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.FILM;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId;
    private final HashMap<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
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

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновление фильма прошло успешно");
        } else {
            throw new NotFoundException(useType(FILM));
        }
        return film;
    }

    @Override
    public Film remove(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.info("Удаление фильма прошло успешно.");
        } else
            throw new NotFoundException(useType(FILM));
        return film;
    }

    @Override
    public Film find(Long id) {
        if (films.containsKey(id)) {
            log.info("Фильм успешно найден");
            return films.get(id);
        } else
            throw new NotFoundException(useType(FILM));
    }

    @Override
    public List<Film> getAll() {
        log.info("Список фильмов успешно получен");
        return new ArrayList<>(films.values());
    }
}
