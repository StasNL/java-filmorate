package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.model.utils.FriendStatus.*;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User removeUser(Long id) {
        return userStorage.removeUser(id);
    }

    public User findUser(Long id) {
        return userStorage.findUser(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);

        if (user.getRelations() == null)
            user.setRelations(new HashMap<>());

        if (friend.getRelations() == null)
            friend.setRelations(new HashMap<>());

        if (!user.getRelations().containsKey(friendId)) {
            user.getRelations().put(friendId, SUBSCRIPTION.getStatus());
            friend.getRelations().put(userId, SUBSCRIBER.getStatus());
            return;
        }

        String userFriendStatus = user.getRelations().get(friendId);
        if (SUBSCRIBER.getStatus().equals(userFriendStatus)) {
            user.getRelations().put(friendId, FRIEND.getStatus());
            friend.getRelations().put(userId, FRIEND.getStatus());
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = findUser(userId);
        User friend = findUser(friendId);
        Map<Long, String> relations = user.getRelations();
        if (relations == null || !relations.containsKey(friendId)
                || relations.get(friendId).equals(SUBSCRIBER.getStatus()))
            throw new NotFoundException("Данного пользователя нет в списке друзей и подписок");

        if (relations.get(friendId).equals(FRIEND.getStatus())) {
            user.getRelations().put(friendId, SUBSCRIBER.getStatus());
            friend.getRelations().put(userId, SUBSCRIPTION.getStatus());
        } else {
            user.getRelations().remove(friendId);
            friend.getRelations().remove(userId);
        }
    }

    public List<User> getFriends(Long userId) {
        User user = findUser(userId);
        Map<Long, String> relations = user.getRelations();
        List<User> friends = new ArrayList<>();
        for (Map.Entry<Long, String> relation : relations.entrySet()) {
            String status = relation.getValue();
            if (status.equals(FRIEND.getStatus())) {
                long friendId = relation.getKey();
                friends.add(findUser(friendId));
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Long user1Id, Long user2Id) {
        List<User> user1Friends = getFriends(user1Id);
        List<User> user2Friends = getFriends(user2Id);
        user1Friends.retainAll(user2Friends);

        return user1Friends;
    }
}
