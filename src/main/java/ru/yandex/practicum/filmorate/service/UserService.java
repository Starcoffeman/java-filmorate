package ru.yandex.practicum.filmorate.service;

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
    public User createUser(@RequestBody @Valid User user) {
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> removeUser(@PathVariable int id) {
        if (inMemoryUserStorage.users.get(id) != null) {
            inMemoryUserStorage.removeUser(id);
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        if (inMemoryUserStorage.users.get(id) != null) {
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
        } else {
            return ResponseEntity.noContent().build();
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
    public ResponseEntity<Integer> addFriend(@PathVariable int id, int friendId) {
        if (inMemoryUserStorage.users.get(id) != null) {
            inMemoryUserStorage.users.get(id).getFriendsList().add(friendId);
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id).getFriendsList().get(friendId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable int id, int friendId) {
        if (inMemoryUserStorage.users.get(id) != null) {
            if (inMemoryUserStorage.users.get(id).getFriendsList().get(friendId) != null) {
                inMemoryUserStorage.users.get(id).getFriendsList().remove(friendId);
                return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
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