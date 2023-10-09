package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@Service
@RequestMapping("/users")
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @PostMapping
    public User createFilm(@RequestBody @Valid User user) {
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        inMemoryUserStorage.removeUser(id);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return inMemoryUserStorage.users.get(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable User updateUser) throws UserNotFoundException {
        inMemoryUserStorage.updateUser(updateUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id,@PathVariable Integer friendId) {
        inMemoryUserStorage.users.get(id).getFriendsList().add(friendId);
        inMemoryUserStorage.users.get(friendId).getFriendsList().add(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        inMemoryUserStorage.users.get(id).getFriendsList().remove(friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ArrayList<Integer> getCommonFriends(@PathVariable Integer id,@PathVariable Integer otherId) {
        ArrayList<Integer> commonFriends = new ArrayList<>();
        for (Integer i : inMemoryUserStorage.users.get(id).getFriendsList()) {
            for (Integer j : inMemoryUserStorage.users.get(otherId).getFriendsList()) {
                if (i == j) {
                    commonFriends.add(i);
                }
            }
        }
        return commonFriends;
    }

}
