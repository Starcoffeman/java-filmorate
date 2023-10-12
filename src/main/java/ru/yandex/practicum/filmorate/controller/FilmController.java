
package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    @Autowired
    FilmService filmService = new FilmService();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllUser() {
        return ResponseEntity.ok(filmService.getAllFilm());
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film updateFilm) throws InternalError, UserNotFoundException {
        filmService.updateFilm(updateFilm);
        return ResponseEntity.ok(updateFilm);
    }


}

