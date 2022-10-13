package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@Getter
@RequestMapping("/users")
public class UserController {

    int userId;
    HashMap<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User userToMemory = user.create(++userId);
        users.put(userToMemory.getId(), userToMemory);
        log.info("Пользователь успешно зарегистрирован.");
        return userToMemory;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        User userToMemory;
        if (users.containsKey(user.getId())) {
            userToMemory = users.get(user.getId()).update(user);
            log.info("Информация о пользователе успешно обновлена");
        } else {
            throw new RuntimeException("Пользователь не зарегистрирован");
        }
        return userToMemory;
    }

    @GetMapping
    public List<User> getAllFilms() {
        return new ArrayList<>(users.values());
    }
}

