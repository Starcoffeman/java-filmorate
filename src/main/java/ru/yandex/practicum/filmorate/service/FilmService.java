package ru.yandex.practicum.filmorate.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService(JdbcTemplate jdbcTemplate) {
        this.filmStorage = new FilmDbStorage(jdbcTemplate) {
        };
    }

    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(Film updateFilm) throws EntityNotFoundException, IdIsNegativeException {
        return filmStorage.updateFilm(updateFilm);
    }

    public void removeFilmById(Integer id) throws EntityNotFoundException, IdIsNegativeException {
        filmStorage.removeFilm(id);
    }

    public Film getFilmById(Integer id) throws EntityNotFoundException, IdIsNegativeException {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getPopularsFilms(Integer id) {
        return filmStorage.getPopularsFilm(id);
    }

    public void addLike(Integer userId, Integer filmId) {
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) throws IdIsNegativeException {
        if (userId < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        filmStorage.removeLike(filmId, userId);
    }

}

