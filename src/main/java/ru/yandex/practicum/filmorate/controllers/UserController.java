package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int userId;
    private HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
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

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Информация о пользователе успешно обновлена");
        } else {
            throw new RuntimeException("Пользователь не зарегистрирован");
        }
        return user;
    }

    @GetMapping
    public List<User> getAllFilms() {
        return new ArrayList<>(users.values());
    }
}

