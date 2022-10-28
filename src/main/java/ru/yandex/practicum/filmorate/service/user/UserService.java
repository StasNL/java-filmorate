package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public User removeUser(Long id) {
        return userStorage.remove(id);
    }

    public User findUser(Long id) {
        return userStorage.find(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(Long userId) {
        User user = findUser(userId);
        return user.getFriends()
                .stream()
                .map(this::findUser)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        User user1 = findUser(user1Id);
        User user2 = findUser(user2Id);
        return user1.getFriends()
                .stream()
                .filter(user2.getFriends()::contains)
                .map(this::findUser)
                .collect(Collectors.toList());
    }
}
