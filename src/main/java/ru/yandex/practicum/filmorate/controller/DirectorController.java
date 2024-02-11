package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorsService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Director findById(@PathVariable("id") long id) {
        log.info("Выовод режиссера под id {}",id);
        return directorsService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Director> findAll() {
        log.info("Вывод всех режиссеров");
        return directorsService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        log.info("Режиссер создан");
        return directorsService.create(director);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Director update(@Valid @RequestBody Director director) {
        log.info("Режиссер обновлён");
        return directorsService.update(director);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable long id) {
        log.info("Режиссер под id:{id} удалён",id);
        directorsService.delete(id);
    }
}