package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


import javax.validation.Valid;

@Service
@RestController
@RequestMapping("/films")
public class FilmService {

    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        inMemoryFilmStorage.addFilm(film);
        return film;
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable int id) {
        inMemoryFilmStorage.removeFilm(id);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return inMemoryFilmStorage.films.get(id);
    }

    @PutMapping("/{id}")
    public void updateFilm(@PathVariable Film updateFilm) throws UserNotFoundException {
        inMemoryFilmStorage.updateFilm(updateFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable int id, int user_id) {
        inMemoryFilmStorage.films.get(id).getLikes().add(user_id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, int user_id) {
        inMemoryFilmStorage.films.get(id).getLikes().remove(user_id);
    }


}