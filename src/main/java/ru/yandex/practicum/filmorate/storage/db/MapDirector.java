package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;


@AllArgsConstructor
public final class MapDirector {
    public Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("ID"), rs.getString("NAME"));
    }
}
