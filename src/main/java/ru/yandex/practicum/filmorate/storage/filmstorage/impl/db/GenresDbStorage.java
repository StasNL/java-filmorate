package ru.yandex.practicum.filmorate.storage.filmstorage.impl.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.GENRE;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

@Component
public class GenresDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Получает список жанров по id фильма.
     */
    public Set<Genre> getGenresByFilmId(Long filmId) {
        Set<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        String sqlQuery = "SELECT fgm.genre_id, g.NAME FROM film_genre_matching AS fgm " +
                "LEFT JOIN GENRES AS g on fgm.GENRE_ID = g.GENRE_ID " +
                "WHERE film_id = ?";
        List<Genre> genreFromDb = jdbcTemplate.query(sqlQuery, this::rowMapToGenre, filmId);
        genres.addAll(genreFromDb);
        return genres;
    }

    /**
     * Получает список всех жанров.
     */

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, this::rowMapToGenre);
    }

    /**
     * Получает название жанра по его id.
     */

    public Genre getGenreById(Integer genreId) {
        if (checkGenreId(genreId))
            throw new NotFoundException(useType(GENRE));
        String sqlQuery = "SELECT * FROM genres " +
                "WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::rowMapToGenre, genreId);
    }

    private Genre rowMapToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    /**
     * Проверяет входящее значение id жанра на вхождение в поле значений.
     * @return - true, если не входит, false - если входит.
     */
    private boolean checkGenreId(int genreId) {
        String sqlQuery = "SELECT MAX(genre_id) FROM genres";
        Integer genreIdMax = Objects.requireNonNull(jdbcTemplate.queryForObject(sqlQuery, Integer.class));
        return genreIdMax < genreId || genreId <= 0;
    }
}
