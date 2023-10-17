
package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    public FilmService() {
        this.filmStorage = new InMemoryFilmStorage();
    }

    public Collection<Film> getAllFilm() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        return film;
    }

    public void updateFilm(Film updateFilm) throws FilmNotFoundException, IdIsNegativeException {
        filmStorage.updateFilm(updateFilm);
    }

    public void removeFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        filmStorage.removeFilm(id);
    }

    public Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getPopularsFilms(Integer id) {
        return filmStorage.getPopularsFilm(id);
    }

    public void addLike(Integer id, Integer likeId) {
        addLike(id,likeId);
    }

    public void removeLike(Integer id, Integer likeId) {
        removeLike(id,likeId);
    }
}

