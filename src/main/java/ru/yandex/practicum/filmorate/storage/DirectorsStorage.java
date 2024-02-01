package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Map;
import java.util.Optional;

public interface DirectorsStorage {

    Map<Long, Director> findAll(); // получение списка всех режиссеров

    Optional<Director> findById(Long id); // получение режиссера по id

    Director create(Director director); // создание режиссёра

    Director update(Director director); // изменение режиссёра

    void delete(Director director); // удаление режиссёра
}
