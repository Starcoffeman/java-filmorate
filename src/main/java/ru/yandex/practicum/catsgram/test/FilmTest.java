package ru.yandex.practicum.catsgram.test;

import org.junit.Test;
import ru.yandex.practicum.catsgram.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class FilmTest {

    @Test
    public void testValidFilm() {
        int id = 1;
        String name = "Фильм";
        String description = "Описание фильма";
        LocalDate releaseDate = LocalDate.of(2022, 1, 1);
        int duration = 120;

        Film film = new Film(id, name, description, releaseDate, duration);

        assertNotNull(film);
    }

    @Test
    public void testEmptyName() {
        int id = 1;
        String name = null;
        String description = "Описание фильма";
        LocalDate releaseDate = LocalDate.of(2022, 1, 1);
        int duration = 120;

        assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
    }

    @Test
    public void testDescriptionTooLong() {
        int id = 1;
        String name = "Фильм";
        String description = "Очень длинное описание фильма, которое превышает 200 символов, чтобы вызвать ошибку валидацииaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        LocalDate releaseDate = LocalDate.of(2022, 1, 1);
        int duration = 120;

        assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
    }

    @Test
    public void testReleaseDateInPast() {
        int id = 1;
        String name = "Фильм";
        String description = "Описание фильма";
        LocalDate releaseDate = LocalDate.of(1895, 12, 27); // 27 декабря 1895 года
        int duration = 120;

        assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
    }

    @Test
    public void testNegativeDuration() {
        int id = 1;
        String name = "Фильм";
        String description = "Описание фильма";
        LocalDate releaseDate = LocalDate.of(2022, 1, 1);
        int duration = -10;

        assertThrows(ValidationException.class, () -> new Film(id, name, description, releaseDate, duration));
    }
}
