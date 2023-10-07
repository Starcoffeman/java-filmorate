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
    public Film createFilm(@RequestBody @Valid Film film) throws ValidationException {
        try {
            inMemoryFilmStorage.addFilm(film);
            return film;
        } catch (Exception e) {
            throw new ValidationException("a");
        }

    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable int id) {
        inMemoryFilmStorage.removeFilm(id);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) throws UserNotFoundException {
        if (inMemoryFilmStorage.films.get(id) != null) {
            return inMemoryFilmStorage.films.get(id);
        } else {
            throw new UserNotFoundException("Фильма под таким индексом нет");
        }
    }

    @GetMapping()
    public Collection<Film> getAllFilms() {
        return inMemoryFilmStorage.films.values();
    }

    @PutMapping
    public Film updateFilm(@PathVariable Film updateFilm) throws UserNotFoundException {
        inMemoryFilmStorage.updateFilm(updateFilm);
        return updateFilm;
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int id, int userid) {
        inMemoryFilmStorage.films.get(id).getLikes().add(userid);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, int userid) {
        inMemoryFilmStorage.films.get(id).getLikes().remove(userid);
    }
}