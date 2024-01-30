package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Map;
import java.util.Optional;

public interface GenreStorage {
    Map<Long, Genre> findAll();

    Optional<Genre> findById(Long id);
}
