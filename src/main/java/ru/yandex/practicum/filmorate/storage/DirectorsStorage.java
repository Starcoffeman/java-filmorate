package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorsStorage {

    List<Director> findAll(); // получение списка всех режиссеров

    Director findById(Long id); // получение режиссера по id

    Director create(Director director); // создание режиссёра

    int update(Director director); // изменение режиссёра

    int delete(long id); // удаление режиссёра
}
