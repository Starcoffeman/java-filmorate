package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface FilmStorage {

    void addFilm(Film film);

    void removeFilm(int id);

    void updateFilm(Film updateFilm) throws UserNotFoundException;
}
