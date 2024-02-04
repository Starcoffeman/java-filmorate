package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    List<Film> findAll();

    Film findById(Long id);

    void addLike(Long filmId, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Long> getLikesOfFilm(Long filmId);

    Map<Long, Set<Long>> getLikesOfFilm(List<Film> filmIds);

    List<Film> findPopular(Integer count);

    List<Film> getFilmsOfDirectorSortByLikesOrYears(Long id, String sortBy);

    List<Film> findCommonFilms(Long userId, Long friendId);

    List<Film> findRecommendation(Long idUser);
}
