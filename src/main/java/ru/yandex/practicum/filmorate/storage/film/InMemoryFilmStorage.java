package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

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


    @Override
    public List<Film> getPopularsFilm(Integer count) {
        List<Film> popularFilms = new ArrayList<>(films.values());
        Collections.sort(popularFilms, new Comparator<Film>() {
            @Override
            public int compare(Film film1, Film film2) {
                return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
            }
        });
        return popularFilms;
    }

    public void addLike(Integer id, Integer likeId) throws UserNotFoundException {
        if (films.get(id) == null || InMemoryUserStorage.users.get(likeId) == null) {
            throw new UserNotFoundException("Нет такого фильма или пользователя");
        }
        films.get(id).getLikes().add(likeId);
    }

    public void removeLike(Integer id, Integer likeId) throws UserNotFoundException {
        if (films.get(id) == null || InMemoryUserStorage.users.get(likeId) == null) {
            throw new UserNotFoundException("Нет такого фильма или пользователя");
        }
        films.get(id).getLikes().remove(likeId);
    }

}
