package ru.yandex.practicum.filmorate.storage.userstorage.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.USER;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

@Component("UserDbStorage")
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private FriendsDbStorage friends;

    /**
     * Создаёт пользователя в базе данных и заполняет таблицу статусов друзей.
     */

    @Override
    public Optional<User> addUser(User user) {
        String sqlQuery = "INSERT INTO users (name, email, login, birthday)" +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            statement.setString(1, user.checkName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getLogin());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);

        long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(userId);
        friends.addFriendsToUser(user);
        User newUser = findUserById(userId).orElseThrow(() -> new NotFoundException(useType(USER)));

        log.info("Пользователь успешно добавлен.");

        return Optional.of(newUser);
    }

    /**
     * Обновляет данные о пользователе, включая статус друзей.
     */

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "name = ?, email = ?, login = ?, birthday = ?" +
                "WHERE user_id = ?";
        int index = jdbcTemplate.update(sqlQuery
                , user.checkName()
                , user.getEmail()
                , user.getLogin()
                , Date.valueOf(user.getBirthday())
                , user.getId());

        if (index == 0)
            throw new NotFoundException(useType(USER));

        friends.removeFriendsByUserId(user.getId());
        friends.addFriendsToUser(user);

        log.info("Данные пользователя успешно обновлены.");
        return Optional.of(user);
    }

    /**
     * Возвращает пользователя по id.
     */

    @Override
    public Optional<User> findUserById(Long userId) {
        String sqlQuery = "SELECT * FROM users " +
                "WHERE user_id = ?";
        User userFromDb;
        try {
            userFromDb = jdbcTemplate.queryForObject(sqlQuery, this::rowMapToUser, userId);
        } catch (RuntimeException e) {
            throw new NotFoundException(useType(USER));
        }

        if (userFromDb == null)
            throw new NotFoundException(useType(USER));

        log.info("Пользователь успешно найден");

        return Optional.of(userFromDb);
    }

    /**
     * Возвращает список всех пользователей.
     */

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::rowMapToUser);
    }

    private User rowMapToUser(ResultSet rs, int rowNum) throws SQLException {
        long userId = rs.getLong("user_id");
        Set<Long> friendsFromDb = friends.getFriendsIdByUserId(userId);
        return User.builder()
                .id(userId)
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .friends(friendsFromDb)
                .build();
    }
}
