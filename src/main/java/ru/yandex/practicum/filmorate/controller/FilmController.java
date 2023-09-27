package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        log.debug("Фильм создана!");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        films.replace(updatedFilm.getId(), updatedFilm);
        log.debug("Фильм обновлен!");
        return updatedFilm;
    }

    @GetMapping
    public HashMap<Integer, Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }
}
