package ru.yandex.practicum.filmorate.storage.filmstorage.impl.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class LikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавляет лайк к фильму.
     * @param filmId - id фильма.
     * @param userId - id пользователя, добавляющего лайк.
     */
    public void addLike(Long filmId, Long userId) {
        String sql = "INSERT INTO likes(film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    /**
     * Заполнение таблицы лайков для фильма.
     */

    public void addLikesToFilm(Film film) {
        if(film.getLikes() == null || film.getLikes().size() == 0)
            return;
        for (Long userId : film.getLikes()) {
            String sqlQuery = "INSERT INTO likes (film_id, user_id)" +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery,
                    film.getId(),
                    userId);
        }
    }

    /**
     * Удаляет все лайки по id фильма.
     * @param filmId - id фильма.
     */

    public void removeLikesByFilmId (Long filmId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, filmId);
    }

    /**
     * Получает лайки по id фильма.
     */

    public Set<Long> getLikesByFilmId (Long filmId) {
        String sqlQuery = "SELECT user_id FROM likes " +
                "WHERE film_id = ?";

        return new HashSet<>(jdbcTemplate.queryForList(sqlQuery, Long.class, filmId));
    }
}
