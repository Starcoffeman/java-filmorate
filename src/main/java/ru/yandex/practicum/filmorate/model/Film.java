package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Data
@NoArgsConstructor
public class Film {

    @Setter
    @Getter
    private int id;

    @Setter
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Setter
    @Getter
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    @Setter
    @Getter
    private LocalDate releaseDate;

    @Setter
    @Getter
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @Getter
    @Setter
    private int rate;

    @Getter
    @Setter
    private Mpa mpa;

    @Getter
    @Setter
    private Set<Integer> likes = new HashSet<>();

    @Getter
    @Setter
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
