package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa findById(@PathVariable long id) {
        log.info("Вывод Mpa под id {}", id);
        return mpaService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Mpa> findAll() {
        log.info("Вывод всех Mpa");
        return mpaService.findAll();
    }
}