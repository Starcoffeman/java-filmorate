package ru.yandex.practicum.filmorate.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;


    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @NotNull
    private Mpa mpa;

    private Genre genre;
    @Getter
    private Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa, Genre genre) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (releaseDate == null || releaseDate.isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genre = genre;
        this.likes = new HashSet<>();
    }

}
