
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(int id) throws FilmNotFoundException, IdIsNegativeException;

    void updateFilm(Film updateFilm) throws FilmNotFoundException, IdIsNegativeException;

    Collection<Film> getAllFilms();

    Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException;

    List<Film> getPopularsFilm(Integer count);

}

