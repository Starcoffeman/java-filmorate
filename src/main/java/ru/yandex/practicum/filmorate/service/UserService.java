package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.userStorage = new UserDbStorage(jdbcTemplate);
    }

    public Collection<User> getAllUser() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        userStorage.addUser(user);
        return user;
    }

    public void updateUser(User updateUser) throws UserNotFoundException {
        userStorage.updateUser(updateUser);
    }

    public void removeUser(Integer id) throws UserNotFoundException {
        userStorage.removeUser(id);
    }

    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        return userStorage.getUserById(id);
    }

    public List<User> getCommonFriendList(Integer id, Integer otherId) {
        return userStorage.getCommonFriendList(id,otherId);
    }

    public void addFriend(Integer id, Integer friendId) throws IdIsNegativeException {
        userStorage.addFriend(id,friendId);
    }

    public List<User> getFriendListById(Integer id) {
        return userStorage.getFriendListById(id);
    }

    public void removeFriendById(Integer id, Integer friendId) throws UserNotFoundException, IdIsNegativeException {
        userStorage.removeFriendById(id,friendId);
    }

}
