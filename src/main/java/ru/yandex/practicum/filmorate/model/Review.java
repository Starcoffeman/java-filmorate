package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Review {
    private long reviewId;

    @NotBlank
    private String content;

    private Boolean isPositive;

    @NonNull
    private int userId;

    @NonNull
    private int filmId;

    private int useful;

}
