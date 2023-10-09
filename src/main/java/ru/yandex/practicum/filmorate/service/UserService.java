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
import java.util.List;

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
            return ResponseEntity.notFound().build();
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Integer id, @PathVariable Integer friendId) throws UserNotFoundException {
        if (friendId < 1) {
            throw new UserNotFoundException("dad");
        }
        inMemoryUserStorage.putFriends(id, friendId);
        return inMemoryUserStorage.users.get(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> removeFriend(@PathVariable int id, int friendId) {
        if (inMemoryUserStorage.users.get(id) != null) {
            if (inMemoryUserStorage.users.get(id).getFriendsList().get(friendId) != null) {
                inMemoryUserStorage.users.get(id).getFriendsList().remove(friendId);
                inMemoryUserStorage.users.get(friendId).getFriendsList().remove(id);
                return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        ArrayList<User> commonFriends = new ArrayList<>();
        if (inMemoryUserStorage.users.get(id) != null & inMemoryUserStorage.users.get(otherId) != null) {
            if (inMemoryUserStorage.users.get(id).getFriendsList().isEmpty()) {
                return ResponseEntity.ok(commonFriends);
            }
            for (User firstUser : inMemoryUserStorage.users.get(id).getFriendsList()) {
                for (User secondUser : inMemoryUserStorage.users.get(otherId).getFriendsList()) {
                    if (firstUser == secondUser) {
                        commonFriends.add(inMemoryUserStorage.users.get(id));
                    }
                }
            }
            if (commonFriends.isEmpty()) {
                return ResponseEntity.ok(commonFriends);
            }
            return ResponseEntity.ok(commonFriends);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> putCommonFriends(@PathVariable int id) {
        if (inMemoryUserStorage.users.get(id) != null) {
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id).getFriendsList());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<User> putCommonFriends(@PathVariable int id, int otherId) {
        if (inMemoryUserStorage.users.get(id) != null) {
            inMemoryUserStorage.users.get(id).getFriendsList().add(inMemoryUserStorage.users.get(otherId));
            inMemoryUserStorage.users.get(otherId).getFriendsList().add(inMemoryUserStorage.users.get(id));
            return ResponseEntity.ok(inMemoryUserStorage.users.get(id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}