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
import java.util.HashMap;

@RestController
@Service
@RequestMapping("/users")
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @PostMapping
    public ResponseEntity<User> createFilm(@RequestBody @Valid User user) {
        inMemoryUserStorage.addUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUser() {
        return ResponseEntity.ok(inMemoryUserStorage.users.values());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<Integer, User>> removeUser(@PathVariable Integer id) {
        if (inMemoryUserStorage.users.get(id) != null) {
            inMemoryUserStorage.removeUser(id);
            return ResponseEntity.ok(inMemoryUserStorage.users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        if (inMemoryUserStorage.users.get(id) != null) {
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User updateUser) throws UserNotFoundException {
        inMemoryUserStorage.updateUser(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<HashMap<Integer, User>> addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (inMemoryUserStorage.users.get(id) != null & id > 0 & friendId > 0 & inMemoryUserStorage.users.get(friendId) != null) {
            inMemoryUserStorage.users.get(id).getFriendsList().add(friendId);
            inMemoryUserStorage.users.get(friendId).getFriendsList().add(id);
            return ResponseEntity.ok(inMemoryUserStorage.users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<HashMap<Integer, User>> removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (inMemoryUserStorage.users.get(id) != null & inMemoryUserStorage.users.get(friendId) != null) {
            inMemoryUserStorage.users.get(id).getFriendsList().remove(friendId);
            inMemoryUserStorage.users.get(friendId).getFriendsList().remove(id);
            return ResponseEntity.ok(inMemoryUserStorage.users);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<ArrayList<Integer>> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (inMemoryUserStorage.users.get(id) != null & inMemoryUserStorage.users.get(otherId) != null) {
            ArrayList<Integer> commonFriends = new ArrayList<>();
            for (Integer i : inMemoryUserStorage.users.get(id).getFriendsList()) {
                for (Integer j : inMemoryUserStorage.users.get(otherId).getFriendsList()) {
                    if (i == j) {
                        commonFriends.add(i);
                    }
                }
            }
            return ResponseEntity.ok(commonFriends);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
