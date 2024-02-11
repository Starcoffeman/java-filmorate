package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorsStorage;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DirectorDbStorage implements DirectorsStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> findAll() {
        String sqlQuery = "SELECT * FROM DIRECTORS;";
        return jdbcTemplate.query(sqlQuery, MapDirector::mapRowToDirector);
    } // получение списка всех режиссеров

    @Override
    public Director findById(Long id) {
        String sqlQuery = "SELECT * FROM DIRECTORS WHERE ID = ?";
        SqlRowSet directorRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (directorRows.next()) {
            Director director = new Director(directorRows.getLong("ID"),
                    directorRows.getString("NAME"));
            log.info("Найден режиссер с id {}", id);
            return director;
        }
        log.warn("Режиссер с id {} не найден", id);
        throw new ResourceNotFoundException("не найден режиссер с id " + id);
    } // получение режиссера по id

    public Director create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO \"DIRECTORS\" (NAME) VALUES (?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sqlQuery, new String[]{"id"});
                    ps.setString(1, director.getName());
                    return ps;
                },
                keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return findById(director.getId());
    } // создание режиссёра

    @Override
    public int update(Director director) {
        String sqlQuery = "UPDATE \"DIRECTORS\" SET NAME = ? WHERE ID = ?";
        return jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
    } // изменение режиссёра

    public int delete(long id) {
        String sqlQuery = "DELETE FROM DIRECTORS WHERE ID = ?;";
        return jdbcTemplate.update(sqlQuery, id);
    } // удаление режиссёра
}