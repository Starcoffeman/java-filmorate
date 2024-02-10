package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FeedServiceImpl feedService;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return Optional.of(filmStorage.update(film))
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));
    }

    public Long removeById(Long id) {
        return filmStorage.delete(id);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long id) {
        return Optional.of(filmStorage.findById(id))
                .orElseThrow(() -> new ResourceNotFoundException("Фильм не найден"));
    }

    public Film addLike(Long filmId, Long userId) {
        if (filmStorage.findById(filmId) == null) {
            throw new ResourceNotFoundException("Фильм не найден");
        }
        if (userStorage.findById(userId) == null) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }
        filmStorage.addLike(filmId, userId);
        feedService.addFeedAddLike(userId, filmId);
        return filmStorage.findById(filmId);
    }

    public Film removeLike(Long filmId, Long userId) {
        if (filmStorage.findById(filmId) == null) {
            throw new ResourceNotFoundException("Фильм не найден");
        }
        if (userStorage.findById(userId) == null) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }
        filmStorage.deleteLike(filmId, userId);
        feedService.addFeedRemoveLike(userId, filmId);
        return filmStorage.findById(filmId);
    }

    public List<Film> getFilmsOfDirectorSortByLikesOrYears(Long id, String sortBy) {
        List<Film> films = filmStorage.getFilmsOfDirectorSortByLikesOrYears(id, sortBy);
        if (films.isEmpty()) {
            throw new ResourceNotFoundException("Режиссер не найден");
        } else {
            return films;
        }
    }

    public List<Film> findCommonFilms(Long userId, Long friendId) {
        if (userStorage.findById(userId) == null || userStorage.findById(friendId) == null) {
            throw new ResourceNotFoundException("Пользователь не найден");
        }

        return filmStorage.findCommonFilms(userId, friendId);
    }

    public List<Film> searchFilmBy(String query, String by) {
        switch (by) {
            case "director":
            case "title":
            case "director,title":
            case "title,director":
                break;
            default: throw new ResourceNotFoundException("Не найдены параметры поиска");
        }
        return filmStorage.searchFilmBy(query, by);
    }

    public List<Film> getMostPopularByGenreYear(Integer year, Long genreId, Integer limit) {
        return filmStorage.getMostPopularByGenreYear(year, genreId, limit);
    }
}