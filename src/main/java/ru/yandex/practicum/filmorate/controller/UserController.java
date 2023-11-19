package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    private final UserService userService;

    public UserController(JdbcTemplate jdbcTemplate) {
        this.userService = new UserService(jdbcTemplate);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        logger.info("Пользователь создан");
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUser() {
        logger.info("Вывод пользователей");

        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updateUser) throws InternalError, UserNotFoundException {
        userService.updateUser(updateUser);
        logger.info("Обновление пользователя");
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) throws UserNotFoundException, IdIsNegativeException {
        logger.info("Вывод пользователя по id");
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable("id") Integer id) throws UserNotFoundException {
        userService.removeUser(id);
        logger.info("Удаление пользователя по id");
        return ResponseEntity.ok().build();
    }


/*    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendListById(@PathVariable("id") Integer id) throws UserNotFoundException, IdIsNegativeException {
        logger.info("Получение списка друзей у конкретного пользователя");
        return ResponseEntity.ok(userService.getFriendListById(id));
    }

    @GetMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> getFriendById(@PathVariable("id") Integer id,
                                              @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        logger.info("Получение конкретного друзей у конкретного пользователя");
        return ResponseEntity.ok(userService.getFriendById(id, friendId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriendById(@PathVariable("id") Integer id,
                                                @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        userService.addFriend(id, friendId);
        logger.info("Добавление конкретного друзей у конкретного пользователя");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> removeFriendById(@PathVariable("id") Integer id,
                                                   @PathVariable("friendId") Integer friendId) throws UserNotFoundException, IdIsNegativeException {
        userService.removeFriendById(id, friendId);
        logger.info("Удаление конкретного друзей у конкретного пользователя");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriendList(@PathVariable("id") Integer id,
                                                          @PathVariable("otherId") Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        logger.info("Вывод общего списка друзей");
        return ResponseEntity.ok(userService.getCommonFriendList(id, otherId));
    }*/
}
