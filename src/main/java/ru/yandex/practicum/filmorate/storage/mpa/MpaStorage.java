package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpaById(Integer id) throws IdIsNegativeException;

    List<Mpa> getAllMpa();

}
