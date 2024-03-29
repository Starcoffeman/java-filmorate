package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (?, ?, ?, ?, ?);";
        String queryForFilmGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";
        String queryForFilmDirector = "INSERT INTO FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?);";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForFilmGenre, film.getId(), genre.getId());
            }
        }
        // добавляем режиссеров
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(queryForFilmDirector, film.getId(), director.getId());
            }
        }
        return findById(film.getId());
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, RATING_ID = ?, DURATION = ?" +
                " WHERE ID = ?;";
        String queryToDeleteFilmGenres = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?;";
        String queryForUpdateGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?);";

        String queryToDeleteFilmDirectors = "DELETE FROM FILM_DIRECTORS WHERE FILM_ID = ?;";
        String queryForUpdateDirector = "INSERT INTO FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?);";

        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getMpa().getId(), film.getDuration(), film.getId());
        jdbcTemplate.update(queryToDeleteFilmGenres, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(queryForUpdateGenre, film.getId(), genre.getId());
            }
        }
        // обновляем список режиссеров в фильме
        jdbcTemplate.update(queryToDeleteFilmDirectors, film.getId());
        if (!film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                jdbcTemplate.update(queryForUpdateDirector, film.getId(), director.getId());
            }
        }
        return findById(film.getId());
    }

    @Override
    public Long delete(Long id) {
        String query = "DELETE FROM FILMS WHERE id = ?";
        if (jdbcTemplate.update(query, id) == 0) {
            throw new ResourceNotFoundException("Запись в БД не найдена");
        } else {
            return id;
        }
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT F.*, R.ID MPA_ID, R.NAME MPA_NAME FROM FILMS AS F JOIN RATING AS R ON F.RATING_ID = R.ID ";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film findById(Long id) {
        String sqlQuery = "SELECT F.*, R.ID MPA_ID, R.NAME MPA_NAME FROM FILMS AS F JOIN RATING AS R ON F.RATING_ID = R.ID " +
                " WHERE F.ID = ?;";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("ID"))
                    .name(filmRows.getString("NAME"))
                    .description(filmRows.getString("DESCRIPTION"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate())
                    .duration(filmRows.getInt("DURATION"))
                    .mpa(new Mpa(filmRows.getLong("MPA_ID"), filmRows.getString("MPA_NAME")))
                    .build();

            film.setGenres(getGenresOfFilm(id).stream().sorted(Comparator.comparingLong(Genre::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
            film.setLikesUser(new HashSet<>(getLikesOfFilm(film.getId())));

            // получаем список режиссеров фильма
            film.setDirectors(getDirectorsOfFilm(id).stream().sorted(Comparator.comparingLong(Director::getId))
                    .collect(Collectors.toCollection(LinkedHashSet::new)));

            log.info("Найден фильм с id {}", id);
            return film;
        }
        log.warn("Фильм с id {} не найден", id);
        throw new ResourceNotFoundException("Что то не работает");
    }

    private List<Genre> getGenresOfFilm(Long filmId) {
        String queryForFilmGenres = "SELECT FG.FILM_ID, FG.GENRE_ID, G.NAME FROM FILM_GENRE FG" +
                " JOIN GENRE G ON G.ID = FG.GENRE_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmGenres, this::mapRowToGenre, filmId);
    }

    // Получаем список режиссеров
    private List<Director> getDirectorsOfFilm(Long filmId) {
        String queryForFilmDirectors = "SELECT FD.FILM_ID, FD.DIRECTOR_ID AS ID, D.NAME FROM FILM_DIRECTORS FD " +
                "JOIN DIRECTORS D ON D.ID = FD.DIRECTOR_ID WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmDirectors, MapDirector::mapRowToDirector, filmId);
    }

    @Override
    public List<Long> getLikesOfFilm(Long filmId) {
        String queryForFilmLikes = "SELECT USER_ID ID FROM FILM_LIKES WHERE FILM_ID = ?;";
        return jdbcTemplate.query(queryForFilmLikes, this::mapRowToLike, filmId);
    }

    @Override
    public Map<Long, Set<Long>> getLikesOfFilm(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        String queryForFilmLikes = "SELECT FILM_ID, USER_ID FROM FILM_LIKES WHERE FILM_ID IN (?);".replace("?", inSql);

        Map<Long, Set<Long>> result = films.stream().collect(Collectors.toMap(Film::getId, Film::getLikesUser));

        jdbcTemplate.query(queryForFilmLikes, (ResultSet rs) -> {
            long filmId = rs.getLong("FILM_ID");
            long userId = rs.getLong("USER_ID");
            result.get(filmId).add(userId);
        }, result.keySet().toArray());

        return result;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        try {
            String sqlQuery = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?);";
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (Exception e) {
            log.warn("Лайк фильму с id {} от пользователя с id {} уже существует", filmId, userId);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public List<Film> getFilmsOfDirectorSortByLikesOrYears(Long id, String sortBy) {
        String yearSqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, " +
                "r.ID AS MPA_ID, r.name AS MPA_NAME, STRING_AGG(DISTINCT g.id || '-' || g.name, ',') AS genres, " +
                "STRING_AGG(DISTINCT l.user_id, ',') AS likes " +
                "FROM FILM_DIRECTORS fd JOIN FILMS f ON fd.FILM_ID = f.ID " +
                "LEFT JOIN RATING r ON f.RATING_ID = r.ID " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRE g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN film_likes l ON f.ID = l.FILM_ID " +
                "WHERE fd.DIRECTOR_ID = ? " +
                "GROUP BY f.ID " +
                "ORDER BY year(f.RELEASE_DATE)";

        String likeSqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, " +
                "r.ID AS MPA_ID, r.name AS MPA_NAME, STRING_AGG(DISTINCT g.id || '-' || g.name, ',') AS genres, " +
                "STRING_AGG(DISTINCT l.user_id, ',') AS likes, " +
                "LENGTH (STRING_AGG(distinct l.user_id,'' )) AS likes_count " +
                "FROM FILM_DIRECTORS fd JOIN FILMS f ON fd.FILM_ID = f.ID " +
                "LEFT JOIN RATING r ON f.RATING_ID = r.ID " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN GENRE g ON fg.GENRE_ID = g.ID " +
                "LEFT JOIN film_likes l ON f.ID = l.FILM_ID " +
                "WHERE fd.DIRECTOR_ID = ? " +
                "GROUP BY f.ID " +
                "ORDER BY likes_count DESC";

        if (sortBy.equals("year")) {
            return Optional.of(jdbcTemplate.query(yearSqlQuery, this::mapRowToFilm, id))
                    .orElse(Collections.emptyList());
        } else {
            return Optional.of(jdbcTemplate.query(likeSqlQuery, this::mapRowToFilm, id))
                    .orElse(Collections.emptyList());
        }
    }

        @Override
        public List<Film> searchFilmBy(String query, String by) {
            String sql = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                    "F.RATING_ID MPA_ID, R.NAME MPA_NAME, " +
                    "COUNT (L.FILM_ID) AS likes_count " +
                    "FROM FILMS f " +
                    "INNER JOIN RATING R ON F.RATING_ID = R.ID " +
                    "LEFT JOIN film_likes l ON f.ID = l.FILM_ID " +
                    "LEFT JOIN film_directors AS fd ON f.id = fd.film_id " +
                    "LEFT JOIN directors AS d ON fd.director_id = d.id " +
                    interpreteQuerry(query, by) +
                    "GROUP BY F.ID " +
                    "ORDER BY likes_count DESC";
        return Optional.of(jdbcTemplate.query(sql, this::mapRowToFilm))
                .orElse(Collections.emptyList());
    }

    private String interpreteQuerry(String query, String by) {
        switch (by) {
            case "title":
                return "WHERE LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
            case "director":
                return "WHERE LOWER(d.name) LIKE " + "LOWER('%" + query + "%') ";
            case "director,title":
            case "title,director":
                return "WHERE LOWER(d.name) LIKE " + "LOWER('%" + query + "%') OR LOWER(f.name) LIKE " + "LOWER('%" + query + "%') ";
            default: return null;
        }
    }

    @Override
    public List<Film> getMostPopularByGenreYear(Optional<Integer> year, Optional<Long> genreId, Integer limit) {
        StringBuilder builder = new StringBuilder()
                .append("SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, ")
                .append("F.DURATION, F.RATING_ID MPA_ID, R.NAME MPA_NAME, COUNT(FL.FILM_ID) RATE, ")
                .append("FROM FILMS F ")
                .append("LEFT JOIN FILM_LIKES FL ON F.ID = FL.FILM_ID ")
                .append("LEFT JOIN RATING R ON F.RATING_ID = R.ID ")
                .append("LEFT JOIN FILM_GENRE FG ON F.ID = FG.FILM_ID ");

        if (year.isPresent() || genreId.isPresent()) {
            builder.append("WHERE ");
            if (year.isPresent()) {
                builder.append("EXTRACT(YEAR FROM F.RELEASE_DATE) = ").append(year.get()).append(" ");
                if (genreId.isPresent()) {
                    builder.append("AND ");
                }
            }
            if (genreId.isPresent()) {
                builder.append("FG.GENRE_ID = ").append(genreId.get()).append(" ");
            }
        }

        builder.append("GROUP BY F.ID " +
                "ORDER BY RATE DESC " +
                "LIMIT ?");

        return jdbcTemplate.query(builder.toString(),
                this::mapRowToFilm,
                limit);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        log.info("Film build start>>>>>");
        Film film = Film.builder()
                .id(rs.getLong("ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(new Mpa(rs.getLong("MPA_ID"), rs.getString("MPA_NAME")))
                .build();
        log.info("Film = {}", film);

        film.setGenres(getGenresOfFilm(film.getId()).stream().sorted(Comparator.comparingLong(Genre::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
        // получаем информацию про режиссеров фильма
        film.setDirectors(getDirectorsOfFilm(film.getId()).stream().sorted(Comparator.comparingLong(Director::getId))
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        film.setLikesUser(new HashSet<>(getLikesOfFilm(film.getId())));
        return film;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getLong("GENRE_ID"), rs.getString("NAME"));
    }

    private Long mapRowToLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("ID");
    }

    @Override
    public List<Film> findCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, \n" +
                "f.RELEASE_DATE, f.DURATION, f.RATING_ID AS MPA_ID, \n" +
                "r.name AS mpa_name, \n" +
                "COUNT(fl3.user_id) AS likes\n" +
                "FROM FILMS f \n" +
                "LEFT JOIN RATING r ON f.RATING_ID = r.ID\n" +
                "LEFT JOIN film_likes fl ON f.id = fl.film_id\n" +
                "LEFT JOIN film_likes fl2 ON f.id = fl2.film_id\n" +
                "LEFT JOIN film_likes fl3 ON f.id = fl3.film_id\n" +
                "WHERE fl.user_id = ? AND fl2.user_id = ? \n" +
                "GROUP BY f.ID \n" +
                "ORDER BY likes DESC;";

        return Optional.of(jdbcTemplate.query(sqlQuery, this::mapRowToFilm, userId, friendId))
                .orElse(Collections.emptyList());
    }

    public List<Film> findRecommendation(Long idUser) {
        List films = new ArrayList<>();
        List users = usersWithSimilarLikes(idUser);
        if (!users.isEmpty()) {
            List<Long> recommendations = filmRecommendations(idUser, users);
            for (Long id : recommendations) {
                films.add(findById(id));
            }
        }
        return films;
    }

    private List<Long> usersWithSimilarLikes(Long userId) {
        final String sqlQuery = "SELECT fl.user_id, COUNT(fl.film_id) RATE FROM film_likes ul" +
                " JOIN film_likes fl  ON (ul.film_id = fl.film_id AND ul.user_id != fl.user_id)" +
                " JOIN users u  ON (fl.user_id != u.id)" +
                " WHERE  ul.user_id = ? " +
                " GROUP BY fl.user_id " +
                " HAVING RATE > 1 " +
                " ORDER BY RATE DESC";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum)
                -> rs.getLong("USER_ID"), userId);
    }

    private List<Long> filmRecommendations(Long userId, List<Long> UserIds) {
        String inSql = String.join(",", Collections.nCopies(UserIds.size(), "?"));
        final String sqlQuery = "SELECT fl.film_id FROM film_likes fl " +
                " WHERE  fl.user_id IN (" + inSql + ")" +
                " AND fl.film_id NOT IN (SELECT ul.film_id FROM film_likes ul WHERE ul.user_id = ?) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum)
                -> rs.getLong("FILM_ID"), UserIds.toArray(), userId);
    }
}