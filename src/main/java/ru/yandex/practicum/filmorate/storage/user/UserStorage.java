package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    void addUser(User user);

    void removeUser(Integer id) throws UserNotFoundException;

    void updateUser(User updateUser) throws UserNotFoundException;

    Collection<User> getAllUsers();

    User getUserById(Integer id) throws UserNotFoundException;

    User getFriendById(Integer id, Integer friend) throws UserNotFoundException;

    void removeFriendById(Integer id, Integer friend) throws UserNotFoundException;

    List<User> getFriendListById(Integer id) throws UserNotFoundException;

    void addFriend(Integer firstId, Integer secondId) throws UserNotFoundException;
    List<User> getCommonFriendList(Integer firstId, Integer secondId) throws UserNotFoundException;

}
