package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.LIKES;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

@Service
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film){
        return filmStorage.add(film);
    }
    public Film update(Film film){
        return filmStorage.update(film);
    }
    public Film remove(Film film){
        return filmStorage.remove(film);
    }
    public Film findFilm(Long filmId){
        return filmStorage.find(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        film.getLikedUsers().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        if(!film.getLikedUsers().remove(userId))
            throw new NotFoundException(useType(LIKES));
    }

    public List<Film> getPopularFilms(Integer count) {
        return getAll()
                .stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getLikedUsers().size(), film1.getLikedUsers().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
