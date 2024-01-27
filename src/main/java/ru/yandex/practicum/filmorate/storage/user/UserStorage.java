package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    void addUser(User user);

    void removeUser(Integer id) throws IdIsNegativeException, EntityNotFoundException;

    User updateUser(User updateUser) throws IdIsNegativeException, EntityNotFoundException;

    Collection<User> getAllUsers();

    User getUserById(Integer id) throws IdIsNegativeException, EntityNotFoundException;

    void removeFriendById(Integer id, Integer otherId) throws IdIsNegativeException, EntityNotFoundException;

    List<User> getFriendListById(Integer id);

    User getFriendById(Integer id, Integer friendId);

    void addFriend(Integer id, Integer friendId) throws IdIsNegativeException;

    List<User> getCommonFriendList(Integer id, Integer otherId);
}
