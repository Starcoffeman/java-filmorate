package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_ValidUser_ReturnsCreatedUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("testuser");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setEmail("test@example.com");
        createdUser.setLogin("testuser");

        when(userService.create(user)).thenReturn(createdUser);

        User result = userController.add(user);

        assertEquals(createdUser, result);
        verify(userService, times(1)).create(user);
    }

    @Test
    void updateUser_ValidUser_ReturnsUpdatedUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setLogin("testuser");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("testuser");

        when(userService.update(user)).thenReturn(updatedUser);
        User result = userController.update(user);
        assertEquals(updatedUser, result);
        verify(userService, times(1)).update(user);
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        List<User> users = Collections.singletonList(new User());

        when(userService.findAll()).thenReturn(users);

        List<User> result = userController.findAll();

        assertEquals(users, result);
        verify(userService, times(1)).findAll();
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userService.findById(userId)).thenReturn(user);

        User result = userController.findById(userId);

        assertEquals(user, result);
        verify(userService, times(1)).findById(userId);
    }

    @Test
    void addFriend_ValidIds_NoContentReturned() {
        Long userId = 1L;
        Long friendId = 2L;

        Assertions.assertDoesNotThrow(() -> userController.addFriend(userId, friendId));

        verify(userService, times(1)).addFriend(userId, friendId);
    }

    @Test
    void removeFriend_ValidIds_NoContentReturned() {
        Long userId = 1L;
        Long friendId = 2L;

        Assertions.assertDoesNotThrow(() -> userController.removeFriend(userId, friendId));

        verify(userService, times(1)).removeFriend(userId, friendId);
    }

    @Test
    void getFriends_ValidId_ReturnsListOfFriends() {
        Long userId = 1L;
        List<User> friends = Collections.singletonList(new User());

        when(userService.findAllFriends(userId)).thenReturn(friends);

        List<User> result = userController.findAllFriends(userId);

        assertEquals(friends, result);
        verify(userService, times(1)).findAllFriends(userId);
    }

    @Test
    void getCommonFriends_ValidIds_ReturnsListOfCommonFriends() {
        Long userId = 1L;
        Long otherId = 2L;
        List<User> commonFriends = Collections.singletonList(new User());

        when(userService.findCommonFriends(userId, otherId)).thenReturn(commonFriends);

        List<User> result = userController.findCommonFriends(userId, otherId);

        assertEquals(commonFriends, result);
        verify(userService, times(1)).findCommonFriends(userId, otherId);
    }
}