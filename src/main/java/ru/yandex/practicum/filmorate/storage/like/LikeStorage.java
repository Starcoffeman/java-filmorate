package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;

public interface LikeStorage {
    void addLike(Integer id, Integer likeId) throws FilmNotFoundException, UserNotFoundException, IdIsNegativeException;

    void removeLike(Integer id, Integer likeId) throws FilmNotFoundException, UserNotFoundException, IdIsNegativeException;
}
