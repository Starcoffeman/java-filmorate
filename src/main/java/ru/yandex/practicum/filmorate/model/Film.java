package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    private int rate;

    @NotNull
    private Mpa mpa;

    private Set<Integer> likes = new HashSet<>();

    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rate = 0;
        this.duration = duration;
        this.mpa = mpa;
    }

}
