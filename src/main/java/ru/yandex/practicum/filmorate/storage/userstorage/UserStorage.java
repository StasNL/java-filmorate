package ru.yandex.practicum.filmorate.storage.userstorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);
    Optional<User> updateUser(User user);
    void removeUser(Long id);
    Optional<User> findUserById(Long id);
    List<User> getAllUsers();
}
