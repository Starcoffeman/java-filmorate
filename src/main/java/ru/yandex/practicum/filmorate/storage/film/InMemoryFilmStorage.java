package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;

public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
    }

    @Override
    public void removeFilm(int id) throws FilmNotFoundException, IdIsNegativeException {
        if (films.get(id) == null) {
            throw new FilmNotFoundException("Фильма под таким id нет");
        }

        if (id < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        films.remove(id);
    }

    @Override
    public void updateFilm(Film updatedFilm) throws FilmNotFoundException, IdIsNegativeException {
        if (films.get(updatedFilm.getId()) == null) {
            throw new FilmNotFoundException("Фильма под таким id нет");
        }

        if (updatedFilm.getId() < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        films.replace(updatedFilm.getId(), updatedFilm);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        if (films.get(id) == null) {
            throw new FilmNotFoundException("Фильма под таким id нет");
        }

        if (id < 1) {
            throw new IdIsNegativeException("Отрицательный id");
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
        if (popularFilms.size() > count) {
            popularFilms = popularFilms.subList(0, count);
        }
        return popularFilms;
    }

    public void addLike(Integer id, Integer likeId) throws FilmNotFoundException, UserNotFoundException, IdIsNegativeException {
        if (films.get(id) == null) {
            throw new FilmNotFoundException("Фильма под таким id нет");
        }

        if (InMemoryUserStorage.users.get(likeId) == null) {
            throw new UserNotFoundException("Пользователя под таким id нет");
        }

        if (id < 1 || likeId < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        films.get(id).getLikes().add(likeId);
    }

    public void removeLike(Integer id, Integer likeId) throws FilmNotFoundException, UserNotFoundException, IdIsNegativeException {
        if (films.get(id) == null) {
            throw new FilmNotFoundException("Фильма под таким id нет");
        }
        if (InMemoryUserStorage.users.get(likeId) == null) {
            throw new UserNotFoundException("Пользователя под таким id нет");
        }

        if (id < 1 || likeId < 1) {
            throw new IdIsNegativeException("Отрицательный id");
        }
        films.get(id).getLikes().remove(likeId);
    }
}
