package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.yandex.practicum.filmorate.validator.ValidReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@SuperBuilder
@NoArgsConstructor
public class Film {
    @EqualsAndHashCode.Exclude
    private long id;
    @NotBlank(message = "название не может быть пустым")
    @NotNull
    private String name;
    @NotNull
    @Size(max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @ValidReleaseDate(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private int duration;
    private Set<Long> likesUser = new HashSet<>();

    private Set<Long> reviews = new HashSet<>();

    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Director> directors = new HashSet<>();
}