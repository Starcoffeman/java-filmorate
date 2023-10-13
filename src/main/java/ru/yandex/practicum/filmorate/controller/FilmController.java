
package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") Integer id) throws InternalError, UserNotFoundException {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeFilmById(@PathVariable("id") Integer id) throws InternalError, UserNotFoundException {
        filmService.removeFilm(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        return filmService.getPopularsFilm(count);
    }

    @PostMapping("/{id}/like/{likeId}")
    public void addLike(@PathVariable("id") Integer id, @PathVariable("likeId") Integer likeId) {
        filmService.addLike(id, likeId);
    }

    @DeleteMapping("/{id}/like/{likeId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("likeId") Integer likeId) {
        filmService.removeLike(id, likeId);
    }


}

