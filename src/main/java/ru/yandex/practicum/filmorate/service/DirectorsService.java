package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorsStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DirectorsService {

    private final DirectorsStorage directorsStorage;

    public Collection<Director> findAll() {
        return Collections.unmodifiableCollection(directorsStorage.findAll().values());
    }

    public Director findById(Long id) {
        return directorsStorage.findById(id).orElseThrow(() -> new ResourceNotFoundException("Что то не работает"));
    }

    public Optional<Director> create(Director director) {
        return directorsStorage.create(director);
    }

    public Director update(Director director) {
        if (directorsStorage.update(director) == 0) {
            throw new ResourceNotFoundException("Ошибка! Невозможно обновить режиссера - его не существует.");
        }
        return findById(director.getId());
    }

    public void delete(long id) {
        if (directorsStorage.delete(id) == 0) {
            throw new ResourceNotFoundException("Ошибка! Невозможно удалить режиссера - его не существует.");
        }
    }
}
