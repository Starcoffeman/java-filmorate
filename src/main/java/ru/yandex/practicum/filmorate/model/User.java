package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private int id;

    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email(message = "Неправильный формат электронной почты")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не может содержать пробелы")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем и должна соответствовать формату yyyy-MM-dd")
    private LocalDate birthday;

    private List<User> friendsList;

    public User(String email, String login, String name, LocalDate birthday) {

        if (name == null || name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }

        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.friendsList = new ArrayList<>();
    }
}
