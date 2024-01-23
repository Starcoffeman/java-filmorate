package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private static final Logger logger = LogManager.getLogger(GenreController.class);

    private final GenreService genreService;

    public GenreController(JdbcTemplate jdbcTemplate) {
        this.genreService = new GenreService(jdbcTemplate);
    }

    @GetMapping
    public ResponseEntity<Collection<Genre>> getAllGenres() {
        logger.info("Жанры выведены");
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable("id") Integer id) throws IdIsNegativeException,
            EntityNotFoundException {
        logger.info("Получение жанра");
        return ResponseEntity.ok(genreService.getGenreById(id));
    }

    @PutMapping
    public ResponseEntity<Genre> updateGenre(@RequestBody @Valid Genre updateGenre) throws InternalError,
            IdIsNegativeException, EntityNotFoundException {
        logger.info("Фильм обновлён");
        return ResponseEntity.ok(genreService.updateGenre(updateGenre));
    }

}
