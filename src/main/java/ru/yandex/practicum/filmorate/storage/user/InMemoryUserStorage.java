package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage{

    public final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(id, user);
    }

    @Override
    public void removeUser(int id) {
        users.remove(id);
    }

    @Override
    public void updateUser(User updatedUser) throws UserNotFoundException {
        if (users.get(updatedUser.getId()) != null) {
            users.replace(updatedUser.getId(), updatedUser);
        } else {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }
    }
}
