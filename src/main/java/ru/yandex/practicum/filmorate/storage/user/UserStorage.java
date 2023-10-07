package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    void addUser(User user);

    void removeUser(int id);

    void updateUser(User updateUser) throws UserNotFoundException;

}
