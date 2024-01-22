package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Component
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(int id) throws IdIsNegativeException, GenreNotFoundException {
        if (id < 0) {
            throw new IdIsNegativeException("Genre ID cannot be negative.");
        }

        String sql = "SELECT * FROM GENRES WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Genre.class), id);
        } catch (EmptyResultDataAccessException e) {
            throw new GenreNotFoundException("Genre with ID " + id + " not found.");
        }
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Genre.class));

    }

    @Override
    public void updateGenre(Genre updateGenre) throws IdIsNegativeException, GenreNotFoundException {
        if (updateGenre.getId() < 0) {
            throw new IdIsNegativeException("Genre ID cannot be negative.");
        }

        getGenreById(updateGenre.getId());

        String sql = "UPDATE GENRES SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, updateGenre.getName(), updateGenre.getId());
    }
}
