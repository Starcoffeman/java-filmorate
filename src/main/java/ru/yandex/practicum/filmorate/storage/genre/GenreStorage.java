package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {

    Genre getGenreById(int id) throws IdIsNegativeException, GenreNotFoundException;

    Collection<Genre> getAllGenres();

    void updateGenre(Genre updateGenre) throws IdIsNegativeException, GenreNotFoundException;


}
