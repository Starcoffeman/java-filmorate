package ru.yandex.practicum.catsgram.test;

import org.junit.Test;
import ru.yandex.practicum.catsgram.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testValidUser() {
        int id = 1;
        String email = "user@example.com";
        String login = "username";
        String name = "User Name";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        User user = new User(id, email, login, name, birthday);

        assertNotNull(user);
    }

    @Test
    public void testInvalidEmail() {
        int id = 1;
        String email = "invalid-email";
        String login = "username";
        String name = "User Name";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
    }

    @Test
    public void testEmptyLogin() {
        // Empty login
        int id = 1;
        String email = "user@example.com";
        String login = "";
        String name = "User Name";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
    }

    @Test
    public void testLoginWithSpace() {
        int id = 1;
        String email = "user@example.com";
        String login = "user name";
        String name = "User Name";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
    }

    @Test
    public void testEmptyName() {
        int id = 1;
        String email = "user@example.com";
        String login = "username";
        String name = "username";
        LocalDate birthday = LocalDate.of(1990, 1, 1);

        User user = new User(id, email, login, name, birthday);

        assertEquals(login, user.getName());
    }

    @Test
    public void testFutureBirthday() {
        int id = 1;
        String email = "user@example.com";
        String login = "username";
        String name = "User Name";
        LocalDate birthday = LocalDate.now().plusDays(1); // Tomorrow

        assertThrows(ValidationException.class, () -> new User(id, email, login, name, birthday));
    }
}
