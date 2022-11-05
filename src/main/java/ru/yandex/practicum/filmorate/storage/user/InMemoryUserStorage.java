package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.USER;
import static ru.yandex.practicum.filmorate.exceptions.NotFoundException.ErrorType.useType;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int userId;
    private final HashMap<Long, User> users = new HashMap<>();

    @Override
    public User add(User user) {
        User userToMemory = User.builder()
                .email(user.getEmail())
                .login(user.getLogin())
                .birthday(user.getBirthday())
                .name(user.checkName())
                .id(++userId)
                .build();
        users.put(userToMemory.getId(), userToMemory);
        log.info("Пользователь успешно зарегистрирован.");
        return userToMemory;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Информация о пользователе успешно обновлена");
        } else {
            throw new NotFoundException(useType(USER));
        }
        return user;
    }

    @Override
    public User remove(Long id) {
        if (users.containsKey(id)) {
            log.info("Удаление фильма прошло успешно.");
            return users.remove(id);
        } else
            throw new NotFoundException(useType(USER));
    }

    @Override
    public User find(Long id) {
        if (users.containsKey(id)) {
            log.info("Пользователь успешно найден");
            return users.get(id);
        } else
            throw new NotFoundException(useType(USER));
    }

    @Override
    public List<User> getAll() {
        log.info("Список пользователей успешно получен");
        return new ArrayList<>(users.values());
    }
}
