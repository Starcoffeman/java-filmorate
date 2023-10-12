package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
    public void updateUser(User updatedUser) throws UserNotFoundException {
        if (users.get(updatedUser.getId()) == null) {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
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
        if (users.get(firstId) == null || users.get(secondId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        users.get(firstId).getFriends().add(users.get(secondId));
        users.get(secondId).getFriends().add(users.get(firstId));
    }


    @Override
    public List<User> getCommonFriendList(Integer firstId, Integer secondId) throws UserNotFoundException {
        List<User> common = new ArrayList<>();
        if (users.get(firstId) == null || users.get(secondId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }

        if(users.get(firstId).getFriends().isEmpty() || users.get(secondId).getFriends().isEmpty() ){
            return common;
        }

        for(User first : users.get(firstId).getFriends()){
            for (User second : users.get(secondId).getFriends()){
                if(first==second){
                    common.add(first);
                }
            }
        }
        return common;
    }
}
