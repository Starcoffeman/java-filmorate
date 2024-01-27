package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {

    Genre getGenreById(int id) throws IdIsNegativeException, EntityNotFoundException;

    Collection<Genre> getAllGenres();

    Genre updateGenre(Genre updateGenre) throws IdIsNegativeException, EntityNotFoundException;

}
