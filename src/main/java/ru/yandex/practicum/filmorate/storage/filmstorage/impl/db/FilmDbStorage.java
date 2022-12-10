package ru.yandex.practicum.filmorate.storage.filmstorage.impl.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.filmstorage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.FILM;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

/**
 * Класс для сохранения в базу данных filmorate.
 */

@Component("FilmDbStorage")
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenresDbStorage genres;
    private final FilmGenresMatchingDbStorage filmGenreMatch;
    private final LikesDbStorage likes;
    private final MpaDbStorage mpa;

    /**
     * Создание фильма в базе данных с присвоением id, заполнением таблиц жанров и лайков.
     */

    @Override
    public Optional<Film> addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);
        filmGenreMatch.addGenresToFilm(film);
        likes.addLikesToFilm(film);
        Film newFilm = findFilmById(filmId).orElseThrow(() -> new NotFoundException(useType(FILM)));

        log.info("Фильм успешно создан.");

        return Optional.of(newFilm);
    }

    /**
     * Обновление фильма вместе с таблицами жанров и лайков.
     */

    @Override
    public Optional<Film> updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?" +
                "WHERE film_id = ?";
        int index = jdbcTemplate.update(sqlQuery,
                film.getName()
                , film.getDescription()
                , Date.valueOf(film.getReleaseDate())
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());

        if (index == 0)
            throw new NotFoundException(useType(FILM));

        filmGenreMatch.removeGenresByFilmId(film.getId());
        likes.removeLikesByFilmId(film.getId());
        filmGenreMatch.addGenresToFilm(film);
        likes.addLikesToFilm(film);

        Film newFilm = findFilmById(film.getId()).orElseThrow(() -> new NotFoundException(useType(FILM)));

        log.info("Фильм успешно обновлён");

        return Optional.of(newFilm);
    }

    /**
     * Возвращает фильм по id.
     */

    @Override
    public Optional<Film> findFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM films " +
                "WHERE film_id = ?";

        Film filmFromDb;
        try {
            filmFromDb = jdbcTemplate.queryForObject(sqlQuery, this::rowMapToFilm, filmId);
        } catch (RuntimeException e) {
            throw new NotFoundException(useType(FILM));
        }
        if (filmFromDb == null)
            throw new NotFoundException(useType(FILM));

        log.info("Фильм успешно найден по id.");

        return Optional.of(filmFromDb);
    }

    /**
     * Возвращает все фильмы.
     */

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::rowMapToFilm);
    }

    private Film rowMapToFilm(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("film_id");
        Set<Genre> genreFromDb = genres.getGenresByFilmId(filmId);
        Set<Long> likesFromDb = likes.getLikesByFilmId(filmId);
        Mpa mpaFromDb = mpa.getMpaByFilmId(filmId);

        Film film = new Film();
        film.setId(filmId);
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
            film.setGenres(genreFromDb);
        film.setLikes(likesFromDb);
        film.setMpa(mpaFromDb);
        return film;


//        return Film.builder()
//                .id(filmId)
//                .name(rs.getString("name"))
//                .description(rs.getString("description"))
//                .releaseDate(rs.getDate("release_date").toLocalDate())
//                .duration(rs.getInt("duration"))
//                .mpa(mpaFromDb)
//                .genres(genreFromDb)
//                .likes(likesFromDb)
//                .build();
    }
}
