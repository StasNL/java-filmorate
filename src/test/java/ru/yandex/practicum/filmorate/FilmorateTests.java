package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.GenresDbStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.LikesDbStorage;
import ru.yandex.practicum.filmorate.storage.filmstorage.impl.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.impl.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.userstorage.impl.UserDbStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateTests {

    // Users
    final UserService userService;
    final UserDbStorage userDbStorage;
    final FriendsDbStorage friends;

//  Films
    final FilmService filmService;
    final FilmDbStorage filmDbStorage;
    final GenresDbStorage genres;
    final LikesDbStorage likes;
    final MpaDbStorage mpa;
    final JdbcTemplate jdbcTemplate;

    @AfterEach
    void clearDb() {
        String sql = "DELETE FROM film_genre_matching; " +
                "DELETE FROM likes; " +
                "DELETE FROM genres; " +
                "ALTER TABLE genres ALTER COLUMN genre_id restart with 1; " +
                "DELETE FROM friends; " +
                "DELETE FROM films; " +
                "ALTER TABLE films ALTER COLUMN film_id restart with 1; " +
                "DELETE FROM users; " +
                "ALTER TABLE users ALTER COLUMN user_id restart with 1; " +
                "DELETE FROM mpa; " +
                "ALTER TABLE mpa ALTER COLUMN mpa_id restart with 1;";

        jdbcTemplate.update(sql);
    }

    //    Users
    @Test
    void addUserTest() {
        User newUser = createUser();// создание пользователя без id.
        User userFromDb = userService.addUser(newUser).get();
        newUser.setId(1L);

        assertEquals(newUser, userFromDb, "Создание пользователя происходит неверно.");
    }

    @Test
    void updateUserTest() {
        User newUser = createUser();
        User anotherUser = createUser();
        anotherUser.setName("anotherName");
        anotherUser.setLogin("anotherLogin");
        anotherUser.setId(1L);

        userService.addUser(newUser).get();
        User anotherUserFromDb = userService.updateUser(anotherUser).get();

        assertEquals(anotherUser, anotherUserFromDb, "Обновление пользователя происходит некорректно.");
    }

    @Test
    void findUserTest() {
        User newUser = createUser();

        User userFromDb = userService.addUser(newUser).get();
        newUser.setId(1L);
        userService.findUser(1L);

        assertEquals(newUser, userFromDb, "Поиск пользователя происходит некорректно.");
    }

    @Test
    void getAllUsersTest() {
        User user1 = createUser();
        user1.setName("User1");

        User user2 = createUser();
        user2.setName("User2");

        userService.addUser(user1);
        userService.addUser(user2);
        user1.setId(1L);
        user2.setId(2L);

        List<User> usersFromDb = userService.getAllUsers();

        assertEquals(2, usersFromDb.size(), "Количество добавленных фильмов и полученных не совпадает");
        assertEquals(user1, usersFromDb.get(0), "Полученные фильмы не соответствуют добавленным.");
        assertEquals(user2, usersFromDb.get(1), "Полученные фильмы не соответствуют добавленным.");
    }

    @Test
    void addFriendTest() {
        User user = createUser();
        user.setName("User");

        User friend = createUser();
        friend.setName("Friend");

        User userFromDb = userService.addUser(user).get();
        User friendFromDb = userService.addUser(friend).get();
        user.setId(1L);
        friend.setId(2L);

//     Проверяем случай невзаимной дружбы.
        userService.addFriend(userFromDb.getId(), friendFromDb.getId());
        userFromDb = userService.findUser(1L).get();
//     Id всех друзей user.
        List<Long> friendsOfUser = new ArrayList<>(userFromDb.getFriends());
        List<Long> friendsOfFriend = new ArrayList<>(friendFromDb.getFriends());

        assertEquals(1, friendsOfUser.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(0, friendsOfFriend.size(), "Дружба не должна быть взаимной.");
        assertEquals(2L, friendsOfUser.get(0), "id добавленного друга и фактический не совпадают.");

//     Проверяем взаимную дружбу.
        userService.addFriend(friendFromDb.getId(), userFromDb.getId());
        userFromDb = userService.findUser(1L).get();
        friendFromDb = userService.findUser(2L).get();

//      ID друзей.
        friendsOfUser = new ArrayList<>(userFromDb.getFriends());
        friendsOfFriend = new ArrayList<>(friendFromDb.getFriends());

        assertEquals(1, friendsOfUser.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(1, friendsOfFriend.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(2L, friendsOfUser.get(0), "id добавленного друга и фактический не совпадают.");
        assertEquals(1L, friendsOfFriend.get(0), "id добавленного друга и фактический не совпадают.");
    }

    @Test
    void removeFriendTest() {
        User user = createUser();
        user.setName("User");

        User friend = createUser();
        friend.setName("Friend");

        User userFromDb = userService.addUser(user).get();
        User friendFromDb = userService.addUser(friend).get();
        user.setId(1L);
        friend.setId(2L);

        userService.addFriend(userFromDb.getId(), friendFromDb.getId());
        userService.removeFriend(userFromDb.getId(), friendFromDb.getId());

        userFromDb = userService.findUser(1L).get();
        List<Long> friendsOfUser = new ArrayList<>(userFromDb.getFriends());

        assertEquals(0, friendsOfUser.size());
    }

    @Test
    void getFriendsTest() {
        User user = createUser();
        user.setName("User");

        User friend1 = createUser();
        friend1.setName("Friend1");

        User friend2 = createUser();
        friend1.setName("Friend2");

        User userFromDb = userService.addUser(user).get();
        User friend1FromDb = userService.addUser(friend1).get();
        User friend2FromDb = userService.addUser(friend2).get();
        user.setId(1L);
        friend1.setId(2L);
        friend2.setId(3L);

        userService.addFriend(userFromDb.getId(), friend1FromDb.getId());
        userService.addFriend(userFromDb.getId(), friend2FromDb.getId());

        List<User> friends = userService.getFriends(userFromDb.getId());

        assertEquals(2, friends.size(), "Количество возвращаемых друзей не соответствует фактическому.");
        assertEquals(friend1, friends.get(0), "Возвращаемый пользователь и фактический не совпадают.");
        assertEquals(friend2, friends.get(1), "Возвращаемый пользователь и фактический не совпадают.");
    }
    @Test
    void getCommonFriendsTest() {
        User user = createUser();
        user.setName("User");

        User friend = createUser();
        friend.setName("Friend1");

        User userFromDb = userService.addUser(user).get();
        User friend1FromDb = userService.addUser(friend).get();

        user.setId(1L);
        friend.setId(2L);

        userService.addFriend(userFromDb.getId(), friend1FromDb.getId());
        userService.addFriend(friend1FromDb.getId(), userFromDb.getId());

        userFromDb = userService.findUser(1L).get();
        friend1FromDb = userService.findUser(2L).get();

        List<User> friendsOfUser = userService.getFriends(userFromDb.getId());
        List<User> friendsOfFriend = userService.getFriends(friend1FromDb.getId());

        assertEquals(friend1FromDb, friendsOfUser.get(0));
        assertEquals(userFromDb, friendsOfFriend.get(0));
    }

    /**
     * Создаёт нового пользователя.
     */

    User createUser() {
        return User.builder()
                .name("newUser")
                .login("newLogin")
                .birthday(LocalDate.of(1985, 12, 1))
                .email("newmail@mail.ru")
                .friends(new HashSet<>())
                .build();
    }
}
