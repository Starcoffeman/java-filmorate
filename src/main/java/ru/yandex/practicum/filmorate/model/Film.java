package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validator.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film {

    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank(message = "название не может быть пустым")
    private String name;

    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;

    @ValidReleaseDate(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "продолжительность фильма должна быть положительной")
    private int duration;

    @NonNull
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Director> directors;
}