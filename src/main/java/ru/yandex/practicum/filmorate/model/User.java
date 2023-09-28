package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.ValidationException;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Некорректный формат электронной почты");
        }

        if (login == null || login.isEmpty() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }

        if (name == null || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }

        LocalDate currentDate = LocalDate.now();
        if (birthday != null && birthday.isAfter(currentDate)) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
