package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class InMemoryUserStorage implements UserStorage {
    public HashMap<Integer, User> users = new HashMap<>();
    public Map<User, List<User>> friendsList = new HashMap<>();

    private int id = 0;

    @Override
    public void addUser(User user) {
        user.setId(++id);
        users.put(id, user);
        friendsList.put(user, new ArrayList<>());
    }

    @Override
    public void removeUser(Integer id) throws UserNotFoundException {
        if (users.get(id) == null) {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }
        friendsList.remove(users.get(id));
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
        friendsList.remove(users.get(id));
    }

    @Override
    public List<User> getFriendListById(Integer id) throws UserNotFoundException {
        if (friendsList.get(users.get(id)) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return friendsList.get(users.get(id));
    }

    @Override
    public User getFriendById(Integer id, Integer friend) throws UserNotFoundException {
        if (users.get(id) == null || users.get(friend) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        return friendsList.get(users.get(id)).get(friend);
    }

    @Override
    public void addFriend(Integer firstId, Integer secondId) throws UserNotFoundException {
        if (users.get(firstId) == null || (firstId < 1 || secondId < 1) || users.get(secondId) == null) {
            throw new UserNotFoundException("Пользователя(-ей) под таким индексом нет");
        }
        friendsList.get(users.get(firstId)).add(users.get(secondId));
        friendsList.get(users.get(secondId)).add(users.get(firstId));
    }

    @Override
    public List<User> getCommonFriendList(Integer firstId, Integer secondId) {
        List<User> common = new ArrayList<>();

        if (users.get(firstId) != null & users.get(secondId) != null) {
            if (friendsList.get(users.get(firstId)).isEmpty() || friendsList.get(users.get(secondId)).isEmpty()) {
                for (User user : friendsList.get(users.get(firstId))) {
                    for (User user1 : friendsList.get(users.get(secondId))) {
                        if (user == user1) {
                            common.add(user);
                        }
                    }
                }
            }
        }
        return common;
    }
}
