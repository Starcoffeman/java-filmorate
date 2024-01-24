package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {

    private final MpaDbStorage mpaStorage;

    public MpaService(JdbcTemplate jdbcTemplate) {
        this.mpaStorage = new MpaDbStorage(jdbcTemplate);
    }

    public Mpa getMpaById(Integer id) throws IdIsNegativeException {
        return mpaStorage.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

}
