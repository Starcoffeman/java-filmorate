package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.db.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(JdbcTemplate jdbcTemplate) {
        this.genreStorage = new GenreDbStorage(jdbcTemplate);
    }

    public Genre getGenreById(int id) throws IdIsNegativeException, GenreNotFoundException {
        return genreStorage.getGenreById(id);
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public void updateGenre(Genre updateGenre) throws IdIsNegativeException, GenreNotFoundException {
        genreStorage.updateGenre(updateGenre);
    }
}
