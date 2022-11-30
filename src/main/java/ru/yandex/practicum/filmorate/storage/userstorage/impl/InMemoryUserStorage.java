package ru.yandex.practicum.filmorate.storage.userstorage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userstorage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.USER;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

@Component("InMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private long userId;
    private long inviteId;
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> addUser(User user) {
        User userToMemory = User.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .name(user.checkName())
                .id(++userId)
                .build();
        users.put(userToMemory.getId(), userToMemory);
        log.info("Пользователь успешно зарегистрирован.");
        return Optional.of(userToMemory);
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Информация о пользователе успешно обновлена");
        } else {
            throw new NotFoundException(useType(USER));
        }
        return Optional.of(user);
    }

    @Override
    public void removeUser(Long id) {
        if (users.containsKey(id)) {
            log.info("Удаление фильма прошло успешно.");
            users.remove(id);
        } else
            throw new NotFoundException(useType(USER));
    }

    @Override
    public Optional<User> findUserById(Long id) {
        if (users.containsKey(id)) {
            log.info("Пользователь успешно найден");
            return Optional.of(users.get(id));
        } else
            throw new NotFoundException(useType(USER));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Список пользователей успешно получен");
        return new ArrayList<>(users.values());
    }
}
