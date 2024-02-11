package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    Review create(Review review);

    Review findById(Long id);

    List<Review> getReviewsByFilmId(int filmId, int count);

    void likeReview(Long reviewId, Long userId);

    void dislikeReview(Long reviewId, Long userId);

    List<Review> findAll();

    Review update(Review review);

    void delete(long id);
}
