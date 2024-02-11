package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM RATING;";

        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa findById(Long id) {
        String sqlQuery = "SELECT * FROM RATING WHERE ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(mpaRows.getLong("ID"), mpaRows.getString("NAME"));
            log.info("Найден рейтинг с id {}", id);
            return mpa;
        }
        log.warn("Рейтинг с id {} не найден", id);
        throw new ResourceNotFoundException("не найден рейтинг с id " + id);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getLong("ID"), rs.getString("NAME"));
    }
}