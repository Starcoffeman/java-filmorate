package ru.yandex.practicum.filmorate.storage.db;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Collection;
import java.util.List;

public class FilmDbStorage implements FilmStorage {
    @Override
    public void addFilm(Film film) {

    }

    @Override
    public void removeFilm(int id) throws FilmNotFoundException, IdIsNegativeException {

    }

    @Override
    public void updateFilm(Film updateFilm) throws FilmNotFoundException, IdIsNegativeException {

    }

    @Override
    public Collection<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        return null;
    }

    @Override
    public List<Film> getPopularsFilm(Integer count) {
        return null;
    }
}
