package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.*;

@Service
@AllArgsConstructor
public class UserService {
    private final @Qualifier("UserDbStorage") UserStorage userStorage;

    public Optional<User> addUser(User user) {
        return userStorage.addUser(user);
    }

    public Optional<User> updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> findUser(Long id) {
        return userStorage.findUserById(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId).orElseThrow(() -> new NotFoundException(useType(USER)));
        User friend = findUser(friendId).orElseThrow(() -> new NotFoundException(useType(USER)));

        user.getFriends().add(friend.getId());
        updateUser(user);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = findUser(userId).orElseThrow(() -> new NotFoundException(useType(USER)));
        User friend = findUser(friendId).orElseThrow(() -> new NotFoundException(useType(USER)));
        user.getFriends().remove(friend.getId());

        updateUser(user);
    }

    public List<User> getFriends(Long userId) {
        User user = findUser(userId).orElseThrow(() -> new NotFoundException(useType(USER)));
        if (user.getFriends() == null)
            throw new NotFoundException(useType(FRIENDS));
        return user.getFriends()
                .stream()
                .map(this::findUser)
                .map(opt -> opt.orElseThrow(() -> new NotFoundException(useType(USER))))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        List<User> user1Friends = getFriends(user1Id);
        List<User> user2Friends = getFriends(user2Id);
        user1Friends.retainAll(user2Friends);

        return user1Friends;
    }
}
