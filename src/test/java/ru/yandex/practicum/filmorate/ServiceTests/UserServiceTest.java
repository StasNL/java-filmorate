package ru.yandex.practicum.filmorate.ServiceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        userService.addFriend(1L, 2L);

        assertEquals(1, user1.getFriends().size());
        assertEquals(1, user2.getFriends().size());

        long id1Friend = (long) user1.getFriends().toArray()[0];
        long id2Friend = (long) user2.getFriends().toArray()[0];
        assertEquals(user2, userService.findUser(id1Friend));
        assertEquals(user1, userService.findUser(id2Friend));
    }

    @Test
    void removeFriend() {
        User user1 = userService.addUser(createUser("User1 for remove Friend test."));
        User user2 = userService.addUser(createUser("Friend"));

        userService.addFriend(1L, 2L);
        userService.removeFriend(2L, 1L);

        assertEquals(0, user1.getFriends().size());
        assertEquals(0, user2.getFriends().size());
    }

    @Test
    void getFriends() {
        User user1 = userService.addUser(createUser("User1 for remove Friend test."));
        User friend1 = userService.addUser(createUser("Friend1"));
        User friend2 = userService.addUser(createUser("Friend2"));

        userService.addFriend(user1.getId(), friend1.getId());
        userService.addFriend(user1.getId(), friend2.getId());

        List<User> friends = userService.getFriends(user1.getId());

        assertEquals(friend1, friends.get(0));
        assertEquals(friend2, friends.get(1));
    }

    @Test
    void getCommonFriends() {
        User user1 = userService.addUser(createUser("User1 for remove Friend test."));
        User friend1 = userService.addUser(createUser("Friend1"));
        User friend2 = userService.addUser(createUser("Friend2"));

        userService.addFriend(user1.getId(), friend1.getId());
        userService.addFriend(user1.getId(), friend2.getId());

        List<User> commonFriends = userService.getCommonFriends(friend1.getId(), friend2.getId());

        assertEquals(1, commonFriends.size());
        assertEquals(user1, commonFriends.get(0));
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