package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Long removeById(Long id);

    List<Film> findAll();

    Film findById(Long id);

    Film addLike(Long filmId, Long userId);

    Film removeLike(Long filmId, Long userId);

    List<Film> getFilmsOfDirectorSortByLikesOrYears(Long id, String sortBy);

    List<Film> findCommonFilms(Long userId, Long friendId);

    List<Film> searchFilmBy(String query, String by);

    List<Film> getMostPopularByGenreYear(Integer year, Long genreId, Integer limit);
}
