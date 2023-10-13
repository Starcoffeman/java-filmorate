
package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;

@Service
public class FilmService {

    final FilmStorage filmStorage = new InMemoryFilmStorage();

    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public void updateFilm(Film updateFilm) throws UserNotFoundException {
        filmStorage.updateFilm(updateFilm);
    }

    public void removeFilm(Integer id) throws UserNotFoundException {
        filmStorage.removeFilm(id);
    }

    public Film getFilmById(Integer id) throws UserNotFoundException {
        return filmStorage.getFilmById(id);
    }


}

