package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(int id) throws UserNotFoundException;

    void updateFilm(Film updateFilm) throws UserNotFoundException;


}
