package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
public class Review {
    private long reviewId;

    @NonNull
    private String content;

    private Boolean isPositive;

    private int userId;

    private int filmId;

    private int useful;

}
