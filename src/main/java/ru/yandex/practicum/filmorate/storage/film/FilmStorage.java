
package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(int id) throws UserNotFoundException;

    void updateFilm(Film updateFilm) throws UserNotFoundException;

    Collection<Film> getAllFilms();

    Film getFilmById(Integer id) throws UserNotFoundException;


}

