package ru.yandex.practicum.filmorate.storage.filmstorage.impl.db;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmGenresMatchingDbStorage {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Заполнение таблицы жанров для фильма.
     */

    public void addGenresToFilm(Film film) {
        if (film.getGenres() == null || film.getGenres().size() == 0)
            return;
        for (Genre genre : film.getGenres()) {
            String sqlQuery = "INSERT INTO FILM_GENRE_MATCHING (film_id, genre_id) " +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    genre.getId());
        }
    }

    /**
     * Удаление жанров фильма по id фильма.
     */

    public void removeGenresByFilmId (Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE_MATCHING WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
