package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import javax.validation.ValidationException;
import java.time.LocalDate;


@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {

        if (name == null) {
            throw new ValidationException("Название не может быть пустым");
        }

        if (description.length() > 200) {
            throw new ValidationException("Описание не может превышать 200 символов");
        }

        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }

        if (duration <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
