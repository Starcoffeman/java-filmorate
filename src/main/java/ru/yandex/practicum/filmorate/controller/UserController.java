package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService = new UserService();


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody @Valid User updateUser) throws InternalError, UserNotFoundException {
        userService.updateUser(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeUserById(@PathVariable("id") Integer id) throws UserNotFoundException {
        userService.removeUser(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriendListById(@PathVariable("id") Integer id) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getFriendListById(id));
    }

    @GetMapping("/{id}/friends/{friendId}")
    public ResponseEntity<User> getFriendById(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getFriendById(id, friendId));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> addFriendById(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        userService.addFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Object> removeFriendById(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        userService.removeFriendById(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriendList(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.getCommonFriendList(id, otherId));
    }
}
