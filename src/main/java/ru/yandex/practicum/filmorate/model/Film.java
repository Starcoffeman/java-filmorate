package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
public class Film {

    int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NonNull
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @PastOrPresent(message = "Дата релиза не может быть в будущем и должна соответствовать формату yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
