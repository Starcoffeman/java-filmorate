package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.FeedServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userService;
    private final FeedServiceImpl feedService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Пользователь создан");
        return userService.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Пользователь обновлён");
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable Long userId) {
        log.info("Пользователь под id:{} удалён",userId);
        userService.removeById(userId);
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Вывод всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        log.info("Вывод пользователя под id {}", id);
        return userService.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Пользователь под userId {} добавил друга под friendId {}", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Пользователь под userId {} удалил друга под friendId {}", userId, friendId);
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Long userId) {
        log.info("Вывод всех пользователей userId {}", userId);
        return userService.findAllFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherUserId) {
        log.info("Вывод общего списка друзей у пользователей под userId {} и otherUserId {}", userId, otherUserId);
        return userService.findCommonFriends(userId, otherUserId);
    }

    @GetMapping("/{idUser}/recommendations")
    public List<Film> findRecommendation(@PathVariable Long idUser) {
        log.info("Вывод рекомендаций для пользователся под idUser {}", idUser);
        return userService.findRecommendation(idUser);
    }

    @GetMapping("/{id}/feed")
    public List<Feed> getFeedsByUserId(@PathVariable long id) {
        log.info("Вывод событий для пользователся под userId {}", id);
        return feedService.getFeedsByUserId(id);
    }

}