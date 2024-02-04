package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Review> findAll() {
        String sqlQuery = "SELECT * FROM reviews ORDER BY useful DESC";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Review(
                rs.getInt("reviewId"),
                rs.getString("content"),
                rs.getBoolean("isPositive"),
                rs.getInt("user_id"),
                rs.getInt("film_id"),
                rs.getInt("useful")
        ));
    }

    @Override
    public List<Review> getReviewsByFilmId(int filmId, int count) {
        String sqlQuery = "SELECT * FROM REVIEWS WHERE film_id = ? ORDER BY useful DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Review(
                rs.getInt("reviewId"),
                rs.getString("content"),
                rs.getBoolean("isPositive"),
                rs.getInt("user_id"),
                rs.getInt("film_id"),
                rs.getInt("useful")
        ), filmId, count);
    }

    @Override
    public Review findById(Long id) {
        String sqlQuery = "SELECT * FROM REVIEWS WHERE reviewId = ?";
        SqlRowSet reviewRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (reviewRows.next()) {
            Review review = new Review(
                    reviewRows.getLong("reviewId"),
                    reviewRows.getString("CONTENT"),
                    reviewRows.getBoolean("ISPOSITIVE"),
                    reviewRows.getInt("USER_ID"),
                    reviewRows.getInt("FILM_ID"),
                    reviewRows.getInt("USEFUL"));
            log.info("Найден отзыв с id {}", id);
            return review;
        } else {
            throw new ResourceNotFoundException("Отзыв с id " + id + " не найден");
        }
    }


    @Override
    public Review create(Review review) {
        if (review.getFilmId() < 0 || review.getUserId() < 0) {
            throw new ResourceNotFoundException("FilmId должен быть положительным числом");
        }

        if (review.getUserId() == 0 || review.getFilmId() == 0) {
            throw new ValidationException("Не были даны userId и filmId");
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sqlQuery = "INSERT INTO \"REVIEWS\" (CONTENT,ISPOSITIVE,USER_ID,FILM_ID,USEFUL) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(sqlQuery, new String[]{"reviewId"});
                    ps.setString(1, review.getContent());
                    ps.setBoolean(2, review.getIsPositive());
                    ps.setInt(3, review.getUserId());
                    ps.setInt(4, review.getFilmId());
                    ps.setInt(5, review.getUseful());
                    return ps;
                },
                keyHolder);
        review.setReviewId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return findById(review.getReviewId());
    }

    @Override
    public Review update(Review updatedReview) {
        String sqlQuery = "UPDATE REVIEWS SET CONTENT=?, ISPOSITIVE=? WHERE reviewId=?";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"reviewId"});
            ps.setString(1, updatedReview.getContent());
            ps.setBoolean(2, updatedReview.getIsPositive());
            ps.setLong(3, updatedReview.getReviewId());
            return ps;
        }, keyHolder);
        return findById(updatedReview.getReviewId());
    }

    @Override
    public void likeReview(Long reviewId, Long userId) {
        String sqlQuery = "INSERT INTO reviews_like (review_id, user_id, is_like) VALUES (?, ?, true)";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
        updateUseful(reviewId, 1);
    }

    @Override
    public void dislikeReview(Long reviewId, Long userId) {
        String sqlQuery = "INSERT INTO reviews_like (review_id, user_id, is_like) VALUES (?, ?, false)";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
        updateUseful(reviewId, -1);
    }

    private void updateUseful(Long reviewId, int value) {
        String sqlQuery = "UPDATE reviews SET useful = useful + ? WHERE reviewId = ?";
        jdbcTemplate.update(sqlQuery, value, reviewId);
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM REVIEWS WHERE reviewId = ?";

        if (jdbcTemplate.update(sqlQuery, id) > 0) {
            log.info("Отзыв с id {} успешно удален", id);
        } else {
            log.warn("Отзыв с id {} не найден и не может быть удален", id);
        }
    }
}
