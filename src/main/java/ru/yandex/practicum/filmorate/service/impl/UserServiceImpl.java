package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FeedServiceImpl feedService;

    public User create(User user) {
        fillEmptyName(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        fillEmptyName(user);
        return Optional.of(userStorage.update(user))
                .orElseThrow(() -> new ResourceNotFoundException("Ошибка! Невозможно обновить пользователя - его не существует."));
    }

    public Long removeById(Long id) {
        return userStorage.delete(id);
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return Optional.of(userStorage.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Не найден пользователь"));
    }

    public User addFriend(Long id, Long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new ResourceNotFoundException("Не найден пользователь");
        }
        userStorage.addFriend(id, friendId);
        feedService.addFeedAddFriend(id, friendId);
        return userStorage.findById(id);
    }

    public User removeFriend(Long id, Long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new ResourceNotFoundException("Не найден пользователь");
        }
        userStorage.removeFriend(id, friendId);
        feedService.addFeedRemoveFriend(id, friendId);
        return userStorage.findById(id);
    }

    public List<User> findAllFriends(Long id) {
        if (userStorage.findById(id) == null) {
            throw new ResourceNotFoundException("Не найден пользователь");
        }
        return userStorage.findAllFriends(id);
    }

    public List<User> findCommonFriends(Long id, Long friendId) {


        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new ResourceNotFoundException("Не найден пользователь");
        }

        Set<User> friends = new HashSet<>(userStorage.findAllFriends(id));
        Set<User> otherFriends = new HashSet<>(userStorage.findAllFriends(friendId));

        if (friends.isEmpty() || otherFriends.isEmpty()) {
            return new ArrayList<>();
        }

        return friends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }

    private void fillEmptyName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    public List<Film> findRecommendation(Long idUser) {
        if (userStorage.findById(idUser) == null) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }
        return filmStorage.findRecommendation(idUser);
    }
}