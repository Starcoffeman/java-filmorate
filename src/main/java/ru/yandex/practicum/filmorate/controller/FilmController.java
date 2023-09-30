package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    Integer id = 0;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        film.setId(++id);
        films.put(id, film);
        log.debug("User создан");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) throws UserNotFoundException {
        if (films.get(updatedFilm.getId()) != null) {
            films.replace(updatedFilm.getId(), updatedFilm);
            log.debug("Фильм обновлен!");
            return updatedFilm;
        } else {
            throw new UserNotFoundException("Пользователя под таким индексом нет  ");
        }
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }
}
