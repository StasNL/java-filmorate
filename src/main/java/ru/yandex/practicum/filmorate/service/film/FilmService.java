package ru.yandex.practicum.filmorate.service.film;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.MpaDbStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.*;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmService {

    private final @Qualifier("FilmDbStorage") FilmStorage filmStorage;
    private final GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpa;
    private final LikesDbStorage likes;

    public Optional<Film> addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> findFilm(Long filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        likes.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = findFilm(filmId).orElseThrow(() -> new NotFoundException(useType(FILM)));
        if (!film.getLikes().remove(userId))
            throw new NotFoundException(useType(LIKES));
        updateFilm(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        return getAllFilms()
                .stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Mpa getMPAById(Integer mpaId) {
        return mpa.getMpaById(mpaId);
    }

    public List<Mpa> getAllMPA() {
        return mpa.getAllMpa();
    }

    public Genre getGenreById(Integer genreId) {
        return genresDbStorage.getGenreById(genreId);
    }

    public List<Genre> getGenres() {
        return genresDbStorage.getAllGenres();
    }
}
