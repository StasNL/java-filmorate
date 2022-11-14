package ru.yandex.practicum.filmorate.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.model.utils.FriendStatus.*;

class UserServiceTest {

    UserService userService;

    @BeforeEach
    void createUserService() {
        this.userService = new UserService(new InMemoryUserStorage());
    }

    @Test
    void addUser() {
        User user = createUser("Add test user.");
        userService.addUser(user);
        long userId = 1;
        user.setId(userId);

        assertEquals(1, userService.getAllUsers().size());
        assertEquals(user, userService.findUser(userId));
    }

    @Test
    void updateUser() {
        User userToAdd = createUser("Created user to update.");
        User userToUpdate = createUser("Updated user");
        userToUpdate.setId(1L);
        userService.addUser(userToAdd);

        userService.updateUser(userToUpdate);

        assertEquals(1, userService.getAllUsers().size());
        assertEquals(userToUpdate, userService.findUser(1L));
    }

    @Test
    void removeUser() {
        User user1 = createUser("User1 for remove test");
        User user2 = createUser("User 2 for remove test.");
        userService.addUser(user1);
        userService.addUser(user2);

        userService.removeUser(1L);

        assertEquals(1, userService.getAllUsers().size());
        assertNotEquals(user1, userService.getAllUsers().get(0));
    }

    @Test
    void findUser() {
        User user = userService.addUser(createUser("User for findUser test"));
        User userFromMemory = userService.findUser(1L);

        assertEquals(user, userFromMemory);
    }

    @Test
    void getAllUsers() {
        User user1 = userService.addUser(createUser("User1 for getAllUsers test."));
        User user2 = userService.addUser(createUser("User2 for getAllUsers test."));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
    }

    @Test
    void addFriend() {
        User user1 = userService.addUser(createUser("User1 for addFriend test."));
        User user2 = userService.addUser(createUser("Friend"));

//     Подписка user1 на user2
        userService.addFriend(1L, 2L);

        assertEquals(1, user1.getRelations().size());
        assertEquals(1, user2.getRelations().size());
        assertEquals(SUBSCRIPTION.getStatus(), user1.getRelations().get(2L));
        assertEquals(SUBSCRIBER.getStatus(), user2.getRelations().get(1L));
//     Взаимная дружба
       userService.addFriend(2L, 1L);

        assertEquals(1, user1.getRelations().size());
        assertEquals(1, user2.getRelations().size());
        assertEquals(FRIEND.getStatus(), user1.getRelations().get(2L));
        assertEquals(FRIEND.getStatus(), user2.getRelations().get(1L));
    }

    @Test
    void removeFriend() {
        User user1 = userService.addUser(createUser("User1 for remove Friend test."));
        User user2 = userService.addUser(createUser("Friend"));
//     Удаление из подписок
        userService.addFriend(1L, 2L);
        userService.removeFriend(1L, 2L);

        assertEquals(0, user1.getRelations().size());
        assertEquals(0, user2.getRelations().size());

//     Удаление из друзей
        userService.addFriend(1L, 2L);
        userService.addFriend(2L, 1L);
        userService.removeFriend(1L, 2L);
//     Пользователь должен остаться, но в качестве подписчика
        assertEquals(1, user1.getRelations().size());
        assertEquals(1, user2.getRelations().size());
        assertEquals(SUBSCRIBER.getStatus(), user1.getRelations().get(2L));
        assertEquals(SUBSCRIPTION.getStatus(), user2.getRelations().get(1L));
    }

    @Test
    void getFriends() {
        User user1 = userService.addUser(createUser("User1."));
        User friend1 = userService.addUser(createUser("Friend1"));
        User friend2 = userService.addUser(createUser("Friend2"));
//     Взаимный друг
        userService.addFriend(user1.getId(), friend1.getId());
        userService.addFriend(friend1.getId(), user1.getId());
//     Подписка
        userService.addFriend(user1.getId(), friend2.getId());

        List<User> friends = userService.getFriends(user1.getId());
//     Должен вывести только друга
        assertEquals(1, friends.size());
        assertEquals(friend1, friends.get(0));
    }

    @Test
    void getCommonFriends() {
        User user1 = userService.addUser(createUser("User1."));
        User user2 = userService.addUser(createUser("User2"));
        User user3 = userService.addUser(createUser("User3"));
//     У поьзователя 1 и пользователя 3 один общий взаимный друг - пользователь 2.
        userService.addFriend(user1.getId(), user2.getId());
        userService.addFriend(user2.getId(), user1.getId());

        userService.addFriend(user3.getId(), user2.getId());
        userService.addFriend(user2.getId(), user3.getId());

        List<User> commonFriends = userService.getCommonFriends(user1.getId(), user3.getId());

        assertEquals(1, commonFriends.size());
        assertEquals(user2, commonFriends.get(0));
//     У остальных общих друзей нет.
        commonFriends = userService.getCommonFriends(user1.getId(), user2.getId());
        assertEquals(0, commonFriends.size());
    }

    User createUser(String userName){
        return User.builder()
                .name(userName)
                .email("user@test.ru")
                .login("loginTest")
                .birthday(LocalDate.of(1985, 12, 1))
                .build();
    }
}