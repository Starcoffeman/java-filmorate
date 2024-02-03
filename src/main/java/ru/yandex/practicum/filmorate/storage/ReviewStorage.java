package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;


public interface ReviewStorage {

    List<Review> findAll();

    List<Review> getReviewsByFilmId(int filmId, int count);

    Review findById(Long id);

    Review create(Review Review);

    Review update(Review Review);

    void delete(long id);

    void likeReview(Long reviewId, Long userId);

    void dislikeReview(Long reviewId, Long userId);
}
