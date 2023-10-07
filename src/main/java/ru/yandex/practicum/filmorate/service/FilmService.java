package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;

@Service
@RestController
@RequestMapping("/films")
public class FilmService {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film)  {
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.films.values();
    }

    @PutMapping
    public Film updateFilm(@PathVariable @Valid Film updateFilm) throws UserNotFoundException {
        inMemoryFilmStorage.updateFilm(updateFilm);
        return updateFilm;
    }
}
