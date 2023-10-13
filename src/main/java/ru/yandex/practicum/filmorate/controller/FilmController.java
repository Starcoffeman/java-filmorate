
package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService = new FilmService();

    @PostMapping
    public Film createFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilm());
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film updateFilm) throws InternalError, FilmNotFoundException, IdIsNegativeException {
        filmService.updateFilm(updateFilm);
        return ResponseEntity.ok(updateFilm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") Integer id) throws InternalError, FilmNotFoundException, IdIsNegativeException {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeFilmById(@PathVariable("id") Integer id) throws InternalError, FilmNotFoundException, IdIsNegativeException {
        filmService.removeFilm(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(name = "count", required = false, defaultValue = "10") int count) {
        return ResponseEntity.ok(filmService.getPopularsFilm(count));
    }

    @PutMapping("/{id}/like/{likeId}")
    public ResponseEntity<Object> addLike(@PathVariable("id") Integer id, @PathVariable("likeId") Integer likeId) throws UserNotFoundException, FilmNotFoundException, IdIsNegativeException {
        filmService.addLike(id, likeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{likeId}")
    public void removeLike(@PathVariable("id") Integer id, @PathVariable("likeId") Integer likeId) throws UserNotFoundException, FilmNotFoundException, IdIsNegativeException {
        filmService.removeLike(id, likeId);

    }

}

