package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final List<User> users = new ArrayList<>(); // Используем ArrayList вместо массива
    private Integer id = 0;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(id);
        users.add(user); // Добавляем пользователя в список
        id++;
        log.debug("User создан");
        return user;
    }

    @PutMapping("/{id}") // Используем путь для указания ID пользователя
    public User updateUser(@PathVariable("id") Integer userId, @RequestBody User updatedUser) throws ValidationException {
        if (userId >= 0 && userId < users.size()) {
            updatedUser.setId(userId); // Устанавливаем ID из пути
            users.set(userId, updatedUser); // Обновляем пользователя в списке
            log.debug("User заменён");
            return updatedUser;
        } else {
            throw new ValidationException("Некорректный ID пользователя");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }
}
