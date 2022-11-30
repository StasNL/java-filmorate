package ru.yandex.practicum.filmorate.storage.filmstorage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
Optional<Film> addFilm(Film film);
Optional<Film> updateFilm(Film film);
void removeFilm(Long filmId);
Optional<Film> findFilmById(Long id);
List<Film> getAllFilms();
}
