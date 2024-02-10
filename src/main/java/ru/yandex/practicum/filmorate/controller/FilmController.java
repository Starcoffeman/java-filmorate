package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм создан film {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Фильм обновлён film {}", film);
        return filmService.update(film);
    }

    @DeleteMapping("/{filmId}")
    public void removeById(@PathVariable Long filmId) {
        log.info("Фильм под filmId{} удалён", filmId);
        filmService.removeById(filmId);
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Вывод всех фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Long id) {
        log.info("Вывод фильма id {}",id);
        return filmService.findById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Пользователь userId {} лайкнул фильм filmId {}", userId, filmId);
        return filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        log.info("Пользователь userId {} дизлайкнул фильм filmId {}", userId, filmId);
        return filmService.removeLike(filmId, userId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsOfDirectorSortByLikesOrYears(@PathVariable Long id,
                                                           @RequestParam(defaultValue = "likes") String sortBy) {
        log.info("Вывод отсортированного список фильмов");
        return filmService.getFilmsOfDirectorSortByLikesOrYears(id, sortBy);
    }

    @GetMapping("/common")
    public List<Film> findCommonFilms(@RequestParam Long userId, @RequestParam Long friendId) {
        log.info("Вывод общего списка фильмов у пользователей userId {} и friendId {}", userId, friendId);
        return filmService.findCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> searchFilmBy(@RequestParam String query, @RequestParam String by) {
        log.info("Поиск фильма осуществлён query {} by {}", query, by);
        return filmService.searchFilmBy(query, by);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularByGenreYear(@RequestParam(defaultValue = "0") Integer year,
                                                @RequestParam(defaultValue = "0") Long genreId,
                                                @RequestParam(defaultValue = "10") @Positive Integer count) {
        log.info("Вывод списка самых популярных фильмов year {} genreId {} count{}", year, genreId, count);
        return filmService.getMostPopularByGenreYear(year, genreId, count);
    }
}