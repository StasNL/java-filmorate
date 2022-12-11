package ru.yandex.practicum.filmorate.storage.filmstorage.impl.db;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.*;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorage {
    JdbcTemplate jdbcTemplate;

    /**
     * Получает рейтинг из базы данных по id фильма.
     */
    public Mpa getMpaByFilmId(Long film_id) {
        String sqlQuery = "SELECT mpa_id FROM films " +
                "WHERE film_id = ?";
        Integer mpaId = jdbcTemplate.queryForObject(sqlQuery, Integer.class, film_id);
        return getMpaById(mpaId);
    }

    /**
     * Получает рейтинг из базы данных по id рейтинга.
     */

    public Mpa getMpaById(Integer mpaId) {
        if(checkMpaId(mpaId))
            throw new NotFoundException(useType(MPA));

        String sqlQuery = "SELECT * FROM mpa " +
                "WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::rowMapToMpa, mpaId);
    }

    /**
     * Получается список всех рейтингов из базы данных.
     */

    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::rowMapToMpa);
    }

    private Mpa rowMapToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }

    /**
     * Проверяет принадлежность id рейтинга полу значений в базе данных.
     * @return - true - не входит, false - входит
     */
    private boolean checkMpaId(Integer mpaId) {
        String sqlQuery = "SELECT MAX(mpa_id) FROM mpa";
        Integer mpaIdMax = Objects.requireNonNull(jdbcTemplate.queryForObject(sqlQuery, Integer.class));
        return mpaIdMax < mpaId || mpaId <= 0;
    }
}
