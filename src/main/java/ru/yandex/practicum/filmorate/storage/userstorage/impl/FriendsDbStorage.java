package ru.yandex.practicum.filmorate.storage.userstorage.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FriendsDbStorage {

    JdbcTemplate jdbcTemplate;

    /**
     * Заполнение списка друзей пользователя.
     */

    public void addFriendsToUser(User user) {
        if (user.getFriends() == null || user.getFriends().size() == 0)
            return;
        user.getFriends()
                .forEach(friendId -> {
                    String sqlQuery = "INSERT INTO friends (user_id, friend_id)" +
                            "VALUES (?, ?)";
                    jdbcTemplate.update(sqlQuery, user.getId(), friendId);
                });
    }

    /**
     * Удаление списка друзей пользователя по его id.
     */

    public void removeFriendsByUserId(Long userId) {

        String sqlQuery = "DELETE FROM friends " +
                "WHERE user_id = ?";

        jdbcTemplate.update(sqlQuery, userId);
    }

    /**
     * Возвращает список друзей по id пользователя.
     */

    public Set<Long> getFriendsIdByUserId(Long userId) {
        String sqlQuery = "SELECT friend_id FROM friends " +
                "WHERE user_id = ?";

        return new HashSet<>(jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("friend_id"), userId));
    }
}

