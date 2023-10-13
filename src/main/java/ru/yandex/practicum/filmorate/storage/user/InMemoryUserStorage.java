package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class InMemoryUserStorage implements UserStorage {
    public HashMap<Integer, User> users = new HashMap<>();

    private int id = 0;

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(id, user);
    }

    @Override
    public void removeUser(Integer id) throws UserNotFoundException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }
        users.remove(id);
    }

    @Override
    public void updateUser(User updatedUser) {
        if (users.get(updatedUser.getId()) == null) {
            throw new InternalError("Пользователя под таким индексом нет");
        }
        users.replace(updatedUser.getId(), updatedUser);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }
        return users.get(id);
    }

    @Override
    public void removeFriendById(Integer id, Integer friend) throws UserNotFoundException {
        if (users.get(id) == null || users.get(friend) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        users.get(id).getFriends().remove(users.get(friend));
    }

    @Override
    public List<User> getFriendListById(Integer id) throws UserNotFoundException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return users.get(id).getFriends();
    }

    @Override
    public User getFriendById(Integer id, Integer friend) throws UserNotFoundException {
        if (users.get(id) == null || users.get(friend) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return users.get(id).getFriends().get(friend);
    }

    @Override
    public void addFriend(Integer firstId, Integer secondId) throws UserNotFoundException {
        if (users.get(firstId) == null || (firstId < 1 || secondId < 1) || users.get(secondId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        List<User> a = users.get(firstId).getFriends();
        a.add(users.get(secondId));
        users.get(firstId).setFriends(a);

        List<User> b = users.get(secondId).getFriends();
        b.add(users.get(firstId));
        users.get(secondId).setFriends(b);
    }

    @Override
    public List<User> getCommonFriendList(Integer firstId, Integer secondId) throws UserNotFoundException {
        List<User> common = new ArrayList<>();
        if (users.get(firstId) != null & users.get(secondId) != null) {
            for (User firstUser : getFriendListById(firstId)) {
                for (User secondUser : getFriendListById(secondId)) {
                    if (firstUser.getId() == secondUser.getId()) {
                        common.add(firstUser);
                    }
                }
            }
        }
        return common;
    }

}
