package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@Component
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void addFilm(Film film) {
        String sqlInsertFilm = "INSERT INTO FILMS (name, description, release_date, rate, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlInsertGenre = "INSERT INTO FILM_GENRES (film_id, genres_id) VALUES (?, ?)";

        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsertFilm, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(film.getReleaseDate().atStartOfDay()));
            ps.setInt(4, film.getRate());
            ps.setInt(5, film.getDuration());
            ps.setInt(6, film.getMpa().getId());
            return ps;
        }, keyHolder);


        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        LinkedHashSet<Genre> genres = film.getGenres();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlInsertGenre, film.getId(), genre.getId());
            }
        }
        film.setLikes(new LinkedHashSet<>());
    }


    @Override
    public void removeFilm(int id) throws FilmNotFoundException, IdIsNegativeException {
        if (id < 0) {
            throw new IdIsNegativeException("Film ID cannot be negative.");
        }

        getFilmById(id);

        String sql = "DELETE FROM FILMS WHERE id = ?";

        int affectedRows = jdbcTemplate.update(sql, id);

        if (affectedRows == 0) {
            throw new FilmNotFoundException("Film with ID " + id + " not found.");
        }
    }

    @Override
    public void updateFilm(Film updateFilm) throws FilmNotFoundException, IdIsNegativeException {
        if (updateFilm.getId() < 0) {
            throw new IdIsNegativeException("Film ID cannot be negative.");
        }

        getFilmById(updateFilm.getId());

        String updateFilmSql = "UPDATE FILMS SET name = ?, description = ?, " +
                "release_date = ?, rate = ?, duration = ?, mpa_id = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(updateFilmSql, updateFilm.getName(), updateFilm.getDescription(),
                Date.valueOf(updateFilm.getReleaseDate()), updateFilm.getRate(),
                updateFilm.getDuration(), updateFilm.getMpa().getId(),
                updateFilm.getId());

        deleteGenresForFilm(updateFilm.getId());

        insertGenresForFilm(updateFilm.getId(), updateFilm.getGenres());
    }

    private void insertGenresForFilm(int filmId, Set<Genre> genres) {
        String sqlInsertGenre = "MERGE INTO FILM_GENRES (film_id, genres_id) VALUES (?, ?)";

        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlInsertGenre, filmId, genre.getId());
            }
        }
    }

    private void deleteGenresForFilm(int filmId) {
        String deleteGenresSql = "DELETE FROM FILM_GENRES WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, filmId);
    }


    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT FILMS.ID AS id, FILMS.NAME, DESCRIPTION, RELEASE_DATE, RATE, DURATION, MPA.ID AS mpa_id, MPA.NAME AS mpa_name, GENRES.ID AS genre_id, GENRES.NAME AS genre_name, " +
                "LIKES.USER_ID AS like_user_id " +
                "FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID = MPA.ID " +
                "LEFT JOIN FILM_GENRES ON FILMS.ID = FILM_GENRES.FILM_ID " +
                "LEFT JOIN GENRES ON FILM_GENRES.GENRES_ID = GENRES.ID " +
                "LEFT JOIN LIKES ON FILMS.ID = LIKES.FILM_ID";

        Map<Integer, Film> filmMap = new HashMap<>();

        jdbcTemplate.query(sql, (rs) -> {
            int filmId = rs.getInt("id");
            Film film = filmMap.getOrDefault(filmId, new Film());
            if (film.getId() == 0) {
                film.setId(filmId);
                film.setName(rs.getString("NAME"));
                film.setDescription(rs.getString("DESCRIPTION"));
                film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
                film.setRate(rs.getInt("RATE"));
                film.setDuration(rs.getInt("DURATION"));

                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("mpa_id"));
                mpa.setName(rs.getString("mpa_name"));
                film.setMpa(mpa);

                film.setGenres(new LinkedHashSet<>());

                filmMap.put(filmId, film);
            }

            int genreId = rs.getInt("genre_id");
            String genreName = rs.getString("genre_name");
            if (genreId > 0 && genreName != null) {
                Genre genre = new Genre();
                genre.setId(genreId);
                genre.setName(genreName);
                film.getGenres().add(genre);
            }

            int likeUserId = rs.getInt("like_user_id");
            if (likeUserId > 0) {
                film.getLikes().add(likeUserId);
            }

        });

        return new ArrayList<>(filmMap.values());
    }


    @Override
    public Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        if (id == null || id < 0) {
            throw new IdIsNegativeException("Film ID cannot be negative.");
        }

        String sql = "SELECT FILMS.ID AS id, FILMS.NAME, DESCRIPTION, RELEASE_DATE, RATE, DURATION, MPA.ID AS mpa_id, " +
                "MPA.NAME AS mpa_name, GENRES.ID AS genre_id, GENRES.NAME AS genre_name " +
                "FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID = MPA.ID " +
                "LEFT JOIN FILM_GENRES ON FILMS.ID = FILM_GENRES.FILM_ID " +
                "LEFT JOIN GENRES ON FILM_GENRES.GENRES_ID = GENRES.ID " +
                "WHERE FILMS.ID = ?";

        try {
            // Use a Map to store films based on their IDs
            Map<Integer, Film> filmMap = new HashMap<>();

            jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setInt(1, id),
                    (rs, rowNum) -> {
                        int filmId = rs.getInt("id");

                        // If the film is already in the map, update it; otherwise, create a new film
                        Film film = filmMap.computeIfAbsent(filmId, key -> {
                            Film newFilm = new Film();
                            newFilm.setId(filmId);
                            try {
                                newFilm.setName(rs.getString("NAME"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                newFilm.setDescription(rs.getString("DESCRIPTION"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                newFilm.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                newFilm.setRate(rs.getInt("RATE"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                newFilm.setDuration(rs.getInt("DURATION"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            Mpa mpa = new Mpa();
                            try {
                                mpa.setId(rs.getInt("mpa_id"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                mpa.setName(rs.getString("mpa_name"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            newFilm.setMpa(mpa);

                            newFilm.setGenres(new LinkedHashSet<>());
                            return newFilm;
                        });

                        int genreId = rs.getInt("genre_id");
                        String genreName = rs.getString("genre_name");
                        if (genreId > 0 && genreName != null) {
                            // Add each genre to the film's set of genres
                            Genre genre = new Genre();
                            genre.setId(genreId);
                            genre.setName(genreName);
                            film.getGenres().add(genre);
                        }

                        return film;
                    });

            // Retrieve the film from the map based on the provided ID
            Film film = filmMap.get(id);

            // Check if the film was found
            if (film == null) {
                throw new FilmNotFoundException("Film with ID " + id + " not found.");
            }

            return film;

        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException("Film with ID " + id + " not found.");
        }
    }


    @Override
    public List<Film> getPopularsFilm(Integer count) {
        if (count == null || count <= 0) {
            throw new IllegalArgumentException("Count should be a positive integer");
        }

        String sql = "SELECT FILMS.ID AS id, FILMS.NAME, DESCRIPTION, RELEASE_DATE, RATE, DURATION, MPA.ID AS mpa_id, MPA.NAME AS mpa_name, " +
                "GENRES.ID AS genre_id, GENRES.NAME AS genre_name, COUNT(LIKES.FILM_ID) AS like_count " +
                "FROM FILMS " +
                "JOIN MPA ON FILMS.MPA_ID = MPA.ID " +
                "LEFT JOIN FILM_GENRES ON FILMS.ID = FILM_GENRES.FILM_ID " +
                "LEFT JOIN GENRES ON FILM_GENRES.GENRES_ID = GENRES.ID " +
                "LEFT JOIN LIKES ON FILMS.ID = LIKES.FILM_ID " +
                "GROUP BY FILMS.ID " +
                "ORDER BY like_count DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, preparedStatement -> preparedStatement.setInt(1, count),
                (rs, rowNum) -> {
                    Film film = new Film();
                    film.setId(rs.getInt("id"));
                    film.setName(rs.getString("NAME"));
                    film.setDescription(rs.getString("DESCRIPTION"));
                    film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
                    film.setRate(rs.getInt("RATE"));
                    film.setDuration(rs.getInt("DURATION"));

                    Mpa mpa = new Mpa();
                    mpa.setId(rs.getInt("mpa_id"));
                    mpa.setName(rs.getString("mpa_name"));
                    film.setMpa(mpa);

                    film.setGenres(new LinkedHashSet<>());

                    int genreId = rs.getInt("genre_id");
                    String genreName = rs.getString("genre_name");
                    if (genreId > 0 && genreName != null) {
                        Genre genre = new Genre();
                        genre.setId(genreId);
                        genre.setName(genreName);
                        film.getGenres().add(genre);
                    }

                    return film;
                });
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        String sqlInsertLike = "INSERT INTO LIKES (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlInsertLike, userId, filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) throws IdIsNegativeException {
        if (filmId < 1 || userId < 1) {
            throw new IdIsNegativeException("Invalid film ID or user ID");
        }

        String sqlDeleteLike = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlDeleteLike, userId, filmId);
    }
}
