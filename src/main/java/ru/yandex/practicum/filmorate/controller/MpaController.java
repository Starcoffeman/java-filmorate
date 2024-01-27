package ru.yandex.practicum.filmorate.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private static final Logger logger = LogManager.getLogger(MpaController.class);

    private final MpaService mpaService;

    public MpaController(JdbcTemplate jdbcTemplate) {
        this.mpaService = new MpaService(jdbcTemplate);
    }

    @GetMapping
    public ResponseEntity<Collection<Mpa>> getAllMpa() {
        logger.info("Вывод пользователей");

        return ResponseEntity.ok(mpaService.getAllMpa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getUserById(@PathVariable("id") Integer id)
            throws IdIsNegativeException {
        logger.info("Вывод пользователя по id");
        return ResponseEntity.ok(mpaService.getMpaById(id));
    }
}
