package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    public final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
    }

    @Override
    public void removeFilm(int id) {
        films.remove(id);
    }

    @Override
    public void updateFilm(Film updatedFilm) throws UserNotFoundException {
        if (films.get(updatedFilm.getId()) != null) {
            films.replace(updatedFilm.getId(), updatedFilm);
        } else {
            throw new UserNotFoundException("Пользователя под таким индексом нет");
        }
    }
}
