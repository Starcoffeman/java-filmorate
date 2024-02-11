package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final FeedService feedService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Пользователь создан");
        return userService.create(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User update(@Valid @RequestBody User user) {
        log.info("Пользователь обновлён");
        return userService.update(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeById(@PathVariable Long userId) {
        log.info("Пользователь под id:{} удалён",userId);
        userService.removeById(userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll() {
        log.info("Вывод всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Long id) {
        log.info("Вывод пользователя под id {}", id);
        return userService.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User addFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Пользователь под userId {} добавил друга под friendId {}", userId, friendId);
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public User removeFriend(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        log.info("Пользователь под userId {} удалил друга под friendId {}", userId, friendId);
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAllFriends(@PathVariable("id") Long userId) {
        log.info("Вывод всех пользователей userId {}", userId);
        return userService.findAllFriends(userId);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> findCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherUserId) {
        log.info("Вывод общего списка друзей у пользователей под userId {} и otherUserId {}", userId, otherUserId);
        return userService.findCommonFriends(userId, otherUserId);
    }

    @GetMapping("/{idUser}/recommendations")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> findRecommendation(@PathVariable Long idUser) {
        log.info("Вывод рекомендаций для пользователся под idUser {}", idUser);
        return userService.findRecommendation(idUser);
    }

    @GetMapping("/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<Feed> getFeedsByUserId(@PathVariable long id) {
        log.info("Вывод событий для пользователся под userId {}", id);
        return feedService.getFeedsByUserId(id);
    }
}