package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
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
    public User getUserById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }

        if (id < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        return users.get(id);
    }

    @Override
    public void removeFriendById(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        if (users.get(id) == null || users.get(otherId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }

        if (id < 1 || otherId < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        users.get(id).getFriends().remove(users.get(otherId));
    }

    @Override
    public List<User> getFriendListById(Integer id) throws UserNotFoundException, IdIsNegativeException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }

        if (id < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }

        return users.get(id).getFriends();
    }

    @Override
    public User getFriendById(Integer id, Integer otherId) throws UserNotFoundException {
        if (users.get(id) == null || users.get(otherId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return users.get(id).getFriends().get(otherId);
    }

    @Override
    public void addFriend(Integer id, Integer otherId) throws UserNotFoundException {
        if (users.get(id) == null || (id < 1 || otherId < 1) || users.get(otherId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        List<User> a = users.get(id).getFriends();
        a.add(users.get(otherId));
        users.get(id).setFriends(a);

        List<User> b = users.get(otherId).getFriends();
        b.add(users.get(id));
        users.get(otherId).setFriends(b);
    }

    @Override
    public List<User> getCommonFriendList(Integer id, Integer otherId) throws UserNotFoundException, IdIsNegativeException {
        List<User> common = new ArrayList<>();
        if (users.get(id) != null & users.get(otherId) != null) {
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
