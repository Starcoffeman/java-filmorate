package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mpa findById(@PathVariable long id) {
        log.info("Вывод Mpa под id {}", id);
        return mpaService.findById(id);
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        log.info("Вывод всех Mpa");
        return mpaService.findAll();
    }
}