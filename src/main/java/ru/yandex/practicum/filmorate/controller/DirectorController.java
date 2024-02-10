package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorsService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorsService directorsService;

    @GetMapping("/{id}")
    public Director findById(@PathVariable("id") long id) {
        log.info("Выовод режиссера под id {}",id);
        return directorsService.findById(id);
    }

    @GetMapping
    public Collection<Director> findAll() {
        log.info("Вывод всех режиссеров");
        return directorsService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        log.info("Режиссер создан director {}", director);
        return directorsService.create(director);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Режиссер обновлён director {}", director);
        return directorsService.update(director);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        log.info("Режиссер под id {} удалён", id);
        directorsService.delete(id);
    }
}