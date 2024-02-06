package ru.yandex.practicum.filmorate.storage.db;

import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;


public final class MapDirector {
    private MapDirector() {
    }

    public static Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return new Director(rs.getLong("ID"), rs.getString("NAME"));
    }
}