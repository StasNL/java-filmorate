package ru.yandex.practicum.filmorate.service.film;

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
public class FilmService {

    FilmStorage filmStorage;
    GenresDbStorage genresDbStorage;
    private final MpaDbStorage mpa;
    private final LikesDbStorage likes;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       GenresDbStorage genresDbStorage,
                       MpaDbStorage mpa,
                       LikesDbStorage likes) {
        this.filmStorage = filmStorage;
        this.genresDbStorage = genresDbStorage;
        this.mpa = mpa;
        this.likes = likes;
    }

    public Optional<Film> add(Film film){
        return filmStorage.addFilm(film);
    }
    public Optional<Film> update(Film film){
        return filmStorage.updateFilm(film);
    }
    public void remove(Long filmId){
        filmStorage.removeFilm(filmId);
    }
    public Optional<Film> findFilm(Long filmId){
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> getAll() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        likes.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = findFilm(filmId).orElseThrow(() -> new NotFoundException(useType(FILM)));
        if(!film.getLikes().remove(userId))
            throw new NotFoundException(useType(LIKES));
        update(film);
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = getAll()
                .stream()
                .sorted((film1, film2) ->
                        Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        return films;
    }

    public Mpa getMPAById (Integer mpaId) {
        return mpa.getMpaById(mpaId);
    }

    public List<Mpa> getAllMPA () {
        return mpa.getAllMpa();
    }

    public Genre getGenreById (Integer genreId) {
        return genresDbStorage.getGenreById(genreId);
    }

    public List<Genre> getGenres () {
        return genresDbStorage.getAllGenres();
    }
}
