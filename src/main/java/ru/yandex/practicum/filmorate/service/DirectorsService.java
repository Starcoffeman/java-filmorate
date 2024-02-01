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

    public Optional<Director> update(Director director) {
        if (directorsStorage.findById(director.getId()).isEmpty()) {
            throw new ResourceNotFoundException("Ошибка! Невозможно обновить режиссера - его не существует.");
        }
        return directorsStorage.update(director);
    }

    public void delete(long id) {
        if (directorsStorage.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Ошибка! Невозможно удалить режиссера - его не существует.");
        }
        directorsStorage.delete(id);
    }

//    DELETE /directors/{id} - Удаление режиссёра
}
