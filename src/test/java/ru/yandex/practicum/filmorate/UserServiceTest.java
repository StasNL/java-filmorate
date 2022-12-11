package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    final UserService userService;

    final JdbcTemplate jdbcTemplate;

    @Test
    void addUserTest() {
        User newUser = createUser();// создание пользователя без id.
        User userFromDb = userService.addUser(newUser).get();
        newUser.setId(userFromDb.getId());

        assertEquals(newUser, userFromDb, "Создание пользователя происходит неверно.");
    }

    @Test
    void updateUserTest() {
        User newUser = createUser();
        userService.addUser(newUser).get();
        User anotherUser = createUser();
        anotherUser.setName("anotherName");
        anotherUser.setLogin("anotherLogin");
        anotherUser.setId(newUser.getId());

        User anotherUserFromDb = userService.updateUser(anotherUser).get();

        assertEquals(anotherUser, anotherUserFromDb, "Обновление пользователя происходит некорректно.");
    }

    @Test
    void findUserTest() {
        User newUser = createUser();

        User userFromDb = userService.addUser(newUser).get();
        newUser.setId(userFromDb.getId());
        userService.findUser(newUser.getId());

        assertEquals(newUser, userFromDb, "Поиск пользователя происходит некорректно.");
    }

    @Test
    void getAllUsersTest() {
//        Удаляем всех пользователей, которые создавались в других тестах.
        String sql = "DELETE FROM USERS";

        jdbcTemplate.update(sql);
        User user1 = createUser();
        user1.setName("User1");

        User user2 = createUser();
        user2.setName("User2");

        User user1FromDb = userService.addUser(user1).get();
        User user2FromDb = userService.addUser(user2).get();
        user1.setId(user1FromDb.getId());
        user2.setId(user2FromDb.getId());

        List<User> usersFromDb = userService.getAllUsers();
//     В предыдущих тестах юыл добавлен 1 фильм. Поэтому общее количество = 3.
//     При добавлении тестов число может изменяться.
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

        long userId = userFromDb.getId();
        long friendId = friendFromDb.getId();
        user.setId(userId);
        friend.setId(friendId);

//     Проверяем случай невзаимной дружбы.
        userService.addFriend(userFromDb.getId(), friendFromDb.getId());
        userFromDb = userService.findUser(userId).get();
//     Id всех друзей user.
        List<Long> friendsOfUser = new ArrayList<>(userFromDb.getFriends());
        List<Long> friendsOfFriend = new ArrayList<>(friendFromDb.getFriends());

        assertEquals(1, friendsOfUser.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(0, friendsOfFriend.size(), "Дружба не должна быть взаимной.");
        assertEquals(friendId, friendsOfUser.get(0), "id добавленного друга и фактический не совпадают.");

//     Проверяем взаимную дружбу.
        userService.addFriend(friendFromDb.getId(), userFromDb.getId());
        userFromDb = userService.findUser(userId).get();
        friendFromDb = userService.findUser(friendId).get();

//      ID друзей.
        friendsOfUser = new ArrayList<>(userFromDb.getFriends());
        friendsOfFriend = new ArrayList<>(friendFromDb.getFriends());

        assertEquals(1, friendsOfUser.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(1, friendsOfFriend.size(),
                "Количество добавленных друзей и фактических не совпадает.");
        assertEquals(friendId, friendsOfUser.get(0), "id добавленного друга и фактический не совпадают.");
        assertEquals(userId, friendsOfFriend.get(0), "id добавленного друга и фактический не совпадают.");
    }

    @Test
    void removeFriendTest() {
        User user = createUser();
        user.setName("User");

        User friend = createUser();
        friend.setName("Friend");

        User userFromDb = userService.addUser(user).get();
        User friendFromDb = userService.addUser(friend).get();

        long userId = userFromDb.getId();
        long friendId = friendFromDb.getId();
        user.setId(userId);
        friend.setId(friendId);

        userService.addFriend(userFromDb.getId(), friendFromDb.getId());
        userService.removeFriend(userFromDb.getId(), friendFromDb.getId());

        userFromDb = userService.findUser(userId).get();
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
        user.setId(userFromDb.getId());
        friend1.setId(friend1FromDb.getId());
        friend2.setId(friend2FromDb.getId());

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

        long userId = userFromDb.getId();
        long friendId = friend1FromDb.getId();

        user.setId(userId);
        friend.setId(friendId);

        userService.addFriend(userFromDb.getId(), friendId);
        userService.addFriend(friend1FromDb.getId(), userId);

        userFromDb = userService.findUser(userId).get();
        friend1FromDb = userService.findUser(friendId).get();

        List<User> friendsOfUser = userService.getFriends(userId);
        List<User> friendsOfFriend = userService.getFriends(friendId);

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
