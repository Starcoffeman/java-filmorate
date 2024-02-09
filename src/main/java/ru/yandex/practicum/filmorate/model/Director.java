package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Director {
    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank(message = "имя режиссера не может быть пустым")
    private String name;
}