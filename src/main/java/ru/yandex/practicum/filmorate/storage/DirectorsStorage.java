package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Map;
import java.util.Optional;

public interface DirectorsStorage {

    Map<Long, Director> findAll(); // получение списка всех режиссеров

    Optional<Director> findById(Long id); // получение режиссера по id

    Optional<Director> create(Director director); // создание режиссёра

    Optional<Director> update(Director director); // изменение режиссёра

    void delete(long id); // удаление режиссёра
}
