package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    private final InMemoryUserStorage userStorage;

    public UserService() {
        this.userStorage = new InMemoryUserStorage();
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

    public User getFriendById(Integer id, Integer otherId) throws UserNotFoundException {
        return userStorage.getFriendById(id, otherId);
    }

    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        userStorage.removeFriendById(id, otherId);
    }

    public void addFriend(Integer id, Integer otherId) throws UserNotFoundException {
        userStorage.addFriend(id, otherId);
    }


    public List<User> getFriendListById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        return userStorage.getFriendListById(id);
    }

    public List<User> getCommonFriendList(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        return userStorage.getCommonFriendList(id, otherId);
    }
}
