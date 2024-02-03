package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@SuperBuilder
@NoArgsConstructor
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
