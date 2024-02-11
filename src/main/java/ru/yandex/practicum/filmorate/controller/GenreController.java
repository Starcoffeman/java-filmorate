package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Genre findById(@PathVariable long id) {
        log.info("Выовод по жанра под id {}", id);
        return genreService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAll() {
        log.info("Вывод всех жанров");
        return genreService.findAll();
    }
}