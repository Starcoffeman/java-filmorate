
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(int id) throws EntityNotFoundException, IdIsNegativeException;

    void updateFilm(Film updateFilm) throws EntityNotFoundException, IdIsNegativeException;

    Collection<Film> getAllFilms();

    Film getFilmById(Integer id) throws EntityNotFoundException, IdIsNegativeException;

    List<Film> getPopularsFilm(Integer count);

    void addLike(Integer id, Integer likeId);

    void removeLike(Integer filmId, Integer userId) throws IdIsNegativeException;
}

