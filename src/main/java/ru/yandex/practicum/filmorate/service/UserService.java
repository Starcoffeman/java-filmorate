package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User update(User user);

    Long removeById(Long id);

    List<User> findAll();

    User findById(Long id);

    User addFriend(Long id, Long friendId);

    User removeFriend(Long id, Long friendId);

    List<User> findAllFriends(Long id);

    List<User> findCommonFriends(Long id, Long friendId);

    List<Film> findRecommendation(Long idUser);
}
