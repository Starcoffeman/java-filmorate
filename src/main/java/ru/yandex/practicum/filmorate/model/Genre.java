package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Genre implements Comparable<Genre> {

    private int id;

    @NotBlank
    private String name;

    @Override
    public int compareTo(Genre genre) {
        if (id == genre.getId()) {
            return 0;
        }
        if (id < genre.getId()) {
            return -1;
        }

        return 1;
    }
}
