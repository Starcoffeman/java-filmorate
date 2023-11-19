package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public MpaService(JdbcTemplate jdbcTemplate) {
        this.mpaStorage = new MpaDbStorage(jdbcTemplate);
    }

    public Mpa getById(int id) throws MpaNotFoundException, UserNotFoundException {
        return mpaStorage.getById(id);
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }
}
