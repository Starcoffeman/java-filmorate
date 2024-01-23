
package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger logger = LogManager.getLogger(FilmController.class);

    private final FilmService filmService;

    public FilmController(JdbcTemplate jdbcTemplate) {
        this.filmService = new FilmService(jdbcTemplate);
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody @Valid Film film) {
        logger.info("Добавление фильма");
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getAllFilm() {
        logger.info("Фильмы выведены");
        return ResponseEntity.ok(filmService.getAllFilm());
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody @Valid Film updateFilm) throws InternalError,
            EntityNotFoundException, IdIsNegativeException {
        logger.info("Фильм обновлён");
        filmService.updateFilm(updateFilm);
        return ResponseEntity.ok(updateFilm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") Integer id) throws InternalError,
            EntityNotFoundException, IdIsNegativeException {
        logger.info("Фильм выведен по id");
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> removeFilmById(@PathVariable("id") Integer id) throws InternalError,
            EntityNotFoundException, IdIsNegativeException {
        filmService.removeFilmById(id);
        logger.info("Фильм удалён по id");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        logger.info("Получен фильм по id");
        return ResponseEntity.ok(filmService.getPopularsFilms(count));
    }


    @PutMapping("/{id}/like/{likeId}")
    public ResponseEntity<Object> addLike(@PathVariable("id") Integer id,
                                          @PathVariable("likeId") Integer likeId) {
        filmService.addLike(likeId, id);
        logger.info("Поставлен лайк");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like/{likeId}")
    public ResponseEntity<Object> removeLike(@PathVariable("id") Integer id,
                                             @PathVariable("likeId") Integer likeId) throws
            IdIsNegativeException {
        filmService.removeLike(id, likeId);
        logger.info("Удалён лайк");
        return ResponseEntity.ok().build();
    }
}

