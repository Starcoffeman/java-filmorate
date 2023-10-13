package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.HashMap;

public class InMemoryFilmStorage implements FilmStorage {

    public HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
    }

    @Override
    public void removeFilm(int id) throws UserNotFoundException {
        if (films.get(id) == null) {
            throw new UserNotFoundException("Нет такого фильма");
        }
        films.remove(id);
    }

    @Override
    public void updateFilm(Film updatedFilm) throws UserNotFoundException {
        if (films.get(updatedFilm.getId()) == null) {
            throw new UserNotFoundException("Фильма под таким id нет");

        }
        films.replace(updatedFilm.getId(), updatedFilm);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Integer id) throws UserNotFoundException {
        if (films.get(id) == null || id < 1) {
            throw new UserNotFoundException("Фильма под таким id нет");
        }
        return films.get(id);
    }


}
