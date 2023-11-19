package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sql = "select * from GENRES";
        return jdbcTemplate.query(sql, GenreDbStorage::createGenre);
    }

    @Override
    public Genre getById(int id) throws GenreNotFoundException {
        String sql = "SELECT * FROM GENRES WHERE id=?";
        List<Genre> genres = jdbcTemplate.query(sql, GenreDbStorage::createGenre, id);
        if (!genres.isEmpty()) {
            return genres.get(0);
        } else {
            throw new GenreNotFoundException("Genre not found");
        }
    }

    static Genre createGenre(ResultSet rs, int rowNuw) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();

    }


}
