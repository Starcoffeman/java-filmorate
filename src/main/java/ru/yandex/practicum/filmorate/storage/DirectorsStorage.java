package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.Map;
import java.util.Optional;

public interface DirectorsStorage {

    Map<Long, Director> findAll(); // получение списка всех режиссеров

    Director findById(Long id); // получение режиссера по id

    Director create(Director director); // создание режиссёра

    int update(Director director); // изменение режиссёра

    int delete(long id); // удаление режиссёра
}
