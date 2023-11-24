package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IdIsNegativeException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Component
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public void addFilm(Film film) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new IllegalArgumentException("Film name cannot be null or empty.");
        }

        Object mpaValue = (film.getMpa() != null) ? film.getMpa().getId() : null;

        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("rate", 0)
                .addValue("duration", film.getDuration())
                .addValue("mpa", mpaValue)
                .addValue("deleted", false);

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(parameters);
        film.setId(generatedId.intValue());
    }


    @Override
    public void removeFilm(int id) throws FilmNotFoundException, IdIsNegativeException {

    }

    @Override
    public void updateFilm(Film updateFilm) throws  IdIsNegativeException {
        if (updateFilm.getId() <= 0 || updateFilm.getId()>=1000 ) {
            throw new IdIsNegativeException("Film ID should be a positive integer.");
        }

        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28); // 28 декабря 1895 года
        if (updateFilm.getReleaseDate() != null && updateFilm.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException("Дата релиза должна быть не ранее 28 декабря 1895 года");
        }

        String sql = "UPDATE FILMS SET " +
                "name = ?, " +
                "description = ?, " +
                "release_date = ?, " +
                "rate = ?, " +
                "duration = ?, " +
                "mpa = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(
                sql,
                updateFilm.getName(),
                updateFilm.getDescription(),
                updateFilm.getReleaseDate(),
                updateFilm.getRate(),
                updateFilm.getDuration(),
                updateFilm.getMpa().getId(),
                updateFilm.getId()
        );
    }



    @Override
    public Film getFilmById(Integer id) throws FilmNotFoundException, IdIsNegativeException {
        return null;
    }

    @Override
    public List<Film> getPopularsFilm(Integer count) {
        return null;
    }
}
