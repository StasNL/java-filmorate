package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
Film add(Film film);
Film update(Film film);
Film remove(Film film);
Film find(Long id);
List<Film> getAll();
}
