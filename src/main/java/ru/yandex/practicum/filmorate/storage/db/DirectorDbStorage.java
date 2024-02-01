package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DirectorDbStorage implements DirectorsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Map<Long, Director> findAll() {
        Map<Long, Director> allDirectors = new HashMap<>();
        String sqlQuery = "SELECT * FROM DIRECTORS;";
        List<Director> directorsFromDb = jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
        for (Director director : directorsFromDb) {
            allDirectors.put(director.getId(), director);
        }
        return allDirectors;
    } // получение списка всех режиссеров

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("ID"), rs.getString("NAME"));
    }

    @Override
    public Optional<Director> findById(Long id) {
        String sqlQuery = "SELECT * FROM DIRECTORS WHERE ID = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
            Director director = new Director(directorRows.getLong("ID"),
                    directorRows.getString("NAME"));
            log.info("Найден режиссер с id {}", id);
            return Optional.of(director);
        }
        log.warn("Режиссер с id {} не найден", id);
        return Optional.empty();
    } // получение режиссера по id

    public Director create(Director director) {
        return null;
    } // создание режиссёра

    public Director update(Director director) {
        return null;
    } // изменение режиссёра

    public void delete(Director director) {
    } // удаление режиссёра

}
