package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpaById(Integer id) throws IdIsNegativeException {
        if (id < 1 || id > 1000) {
            throw new IdIsNegativeException("Mpa ID cannot be negative.");
        }
        String sql = "SELECT * FROM MPA WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Mpa.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Mpa.class));
    }
}
