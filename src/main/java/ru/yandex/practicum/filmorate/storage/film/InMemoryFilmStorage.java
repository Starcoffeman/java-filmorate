package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;

public class InMemoryFilmStorage implements FilmStorage{

    public HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public void addFilm(Film film) {
        film.setId(++id);
        films.put(id, film);
    }

    @Override
    public void removeFilm(int id) throws UserNotFoundException {
        if(films.get(id)!=null){
            films.remove(id);
        } else {
            throw new UserNotFoundException("Нет такого фильма");
        }


    }

    @Override
    public void updateFilm(Film updatedFilm) throws UserNotFoundException {
        if (films.get(updatedFilm.getId()) != null) {
            films.replace(updatedFilm.getId(), updatedFilm);
        } else {
            throw new UserNotFoundException("Пользователя под таким id нет");
        }
    }
}
