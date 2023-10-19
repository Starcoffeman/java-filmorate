package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.ArrayList;
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
        if (userStorage.getUsers().get(id) == null || userStorage.getUsers().get(otherId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return userStorage.getUsers().get(id).getFriends().get(otherId);
    }

    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        userStorage.removeFriendById(id, otherId);
    }

    public void addFriend(Integer id, Integer otherId) throws UserNotFoundException {
        if (userStorage.getUsers().get(id) == null || (id < 1 || otherId < 1) || userStorage.getUsers().get(otherId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        List<User> a = userStorage.getUsers().get(id).getFriends();
        a.add(userStorage.getUsers().get(otherId));
        userStorage.getUsers().get(id).setFriends(a);

        List<User> b = userStorage.getUsers().get(otherId).getFriends();
        b.add(userStorage.getUsers().get(id));
        userStorage.getUsers().get(otherId).setFriends(b);
    }


    public List<User> getFriendListById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (userStorage.getUsers().get(id) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }

        if (id < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }

        return userStorage.getUsers().get(id).getFriends();
    }


    public List<User> getCommonFriendList(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        List<User> common = new ArrayList<>();
        if (userStorage.getUsers().get(id) != null & userStorage.getUsers().get(otherId) != null) {
            for (User firstUser : getFriendListById(id)) {
                for (User secondUser : getFriendListById(otherId)) {
                    if (firstUser.getId() == secondUser.getId()) {
                        common.add(firstUser);
                    }
                }
            }
        }
        return common;
    }
}
