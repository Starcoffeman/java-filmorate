package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    void addUser(User user);

    void removeUser(Integer id) throws UserNotFoundException;

    void updateUser(User updateUser) throws UserNotFoundException;

    Collection<User> getAllUsers();

    User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException;

    User getFriendById(Integer id, Integer otherId) throws UserNotFoundException;

    void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException;

    List<User> getFriendListById(Integer id) throws UserNotFoundException, IdIsNegativeException;

    void addFriend(Integer id, Integer otherId) throws UserNotFoundException;

    List<User> getCommonFriendList(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException;

}
