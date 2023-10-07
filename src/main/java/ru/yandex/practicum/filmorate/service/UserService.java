package ru.yandex.practicum.filmorate.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RestController
@RequestMapping("/users")
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @PostMapping
    public User createFilm(@RequestBody @Valid User user) {
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable int id) {
        inMemoryUserStorage.removeUser(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) throws UserNotFoundException {
        if (inMemoryUserStorage.users.get(id) != null) {
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping()
    public Collection<User> getAllUsers() {
        return inMemoryUserStorage.users.values();
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User updateUser) throws UserNotFoundException {
        inMemoryUserStorage.updateUser(updateUser);
        return updateUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, int friendId) {
        inMemoryUserStorage.users.get(id).getFriendsList().add(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, int friendId) {
        inMemoryUserStorage.users.get(id).getFriendsList().remove(friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ArrayList<Integer> getCommonFriends(@PathVariable int id, int otherId) {
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
