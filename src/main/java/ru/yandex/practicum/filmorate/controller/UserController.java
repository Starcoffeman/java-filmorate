package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    Integer id = 0;

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("User создан");
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody User updatedUser) {
        users.replace(updatedUser.getId(), updatedUser);
        log.debug("User заменён");
        return updatedUser;
    }

    @GetMapping
    public Collection<User> getAllUsers(@RequestBody User user) {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }
}
