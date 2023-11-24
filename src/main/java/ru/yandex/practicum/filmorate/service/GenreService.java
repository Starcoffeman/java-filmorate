package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.genreStorage = new GenreDbStorage(jdbcTemplate);
    }

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(int id) throws GenreNotFoundException {
        return genreStorage.getById(id);
    }
}
