package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@Service
public class UserService {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @PostMapping
    public User createFilm(@RequestBody @Valid User user) {
        inMemoryUserStorage.addUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable int id){
        inMemoryUserStorage.removeUser(id);
    }

    @GetMapping("/{id}")
    public User getUserById( @PathVariable int id){
        return inMemoryUserStorage.users.get(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@PathVariable User updateUser) throws UserNotFoundException {
        inMemoryUserStorage.updateUser(updateUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, int friend_Id){
        inMemoryUserStorage.users.get(id).getFriendsList().add(friend_Id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, int friend_Id){
        inMemoryUserStorage.users.get(id).getFriendsList().remove(friend_Id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ArrayList<Integer> getCommonFriends(@PathVariable int id, int other_Id){
        ArrayList<Integer> commonFriends = new ArrayList<>();
        for(Integer i : inMemoryUserStorage.users.get(id).getFriendsList()){
            for( Integer j : inMemoryUserStorage.users.get(other_Id).getFriendsList()){
                if(i ==j){
                    commonFriends.add(i);
                }
            }
        }
        return commonFriends;
    }

}
