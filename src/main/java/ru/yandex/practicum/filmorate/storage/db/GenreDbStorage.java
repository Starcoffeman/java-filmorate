package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(Long id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRows.next()) {
            Genre genre = new Genre(genreRows.getLong("ID"),
                    genreRows.getString("NAME"));
            log.info("Найден жанр с id {}", id);
            return genre;
        }
        log.warn("Жанр с id {} не найден", id);
        throw new ResourceNotFoundException("не найден жанр с id " + id);
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM GENRE;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("ID"), rs.getString("NAME"));
    }
}

