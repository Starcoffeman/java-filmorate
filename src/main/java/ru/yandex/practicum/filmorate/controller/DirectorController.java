package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorsService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorsService directorsService;

    @GetMapping("/{id}")
    public Director findById(@PathVariable long id) {
        return directorsService.findById(id);
    }

    @GetMapping
    public Collection<Director> getAll() {
        return directorsService.findAll();
    }

    @PostMapping
    public Optional<Director> create(@Valid @RequestBody Director director) {
        return directorsService.create(director);
    }

    @PutMapping
    public Optional<Director> update(@Valid @RequestBody Director director) {
        return directorsService.update(director);
    }

    @DeleteMapping("/{id}")
    public void deleteDirectorById(@PathVariable long id) {
        directorsService.delete(id);
    }
}
