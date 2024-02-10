package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.storage.DirectorsStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorsServiceImpl implements DirectorService {

    private final DirectorsStorage directorsStorage;

    public List<Director> findAll() {
        return directorsStorage.findAll();
    }

    public Director findById(Long id) {
        return Optional.of(directorsStorage.findById(id)).orElseThrow(() -> new ResourceNotFoundException("Не найден режиссер с id = " + id));
    }

    public Director create(Director director) {
        return directorsStorage.create(director);
    }

    public Director update(Director director) {
        if (directorsStorage.update(director) == 0) {
            throw new ResourceNotFoundException("Ошибка! Невозможно обновить режиссера - его не существует.");
        }
        return director;
    }

    public void delete(long id) {
        if (directorsStorage.delete(id) == 0) {
            throw new ResourceNotFoundException("Ошибка! Невозможно удалить режиссера - его не существует.");
        }
        directorsStorage.delete(id);
    }
}
