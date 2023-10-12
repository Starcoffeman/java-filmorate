package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    final UserStorage userStorage = new InMemoryUserStorage();

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

    public User getUserById(Integer id) throws UserNotFoundException {
        return userStorage.getUserById(id);
    }

    public User getFriendById(Integer id, Integer friend) throws UserNotFoundException {
        return userStorage.getFriendById(id, friend);
    }

    public void removeFriendById(Integer id, Integer friend) throws UserNotFoundException {
        userStorage.removeFriendById(id, friend);
    }

    public void addFriend(Integer firstId, Integer secondId) throws UserNotFoundException {
        userStorage.addFriend(firstId,secondId);
    }


    public List<User> getFriendListById(Integer id) throws UserNotFoundException {
        return userStorage.getFriendListById(id);
    }

    public List<User> getCommonFriendList(Integer firstId, Integer secondId) throws UserNotFoundException {
        return userStorage.getCommonFriendList(firstId, secondId);
    }

}
