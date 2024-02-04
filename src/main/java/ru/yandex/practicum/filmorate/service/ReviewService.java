package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewStorage reviewStorage;

    public Review create(Review review) {
        try {
            return reviewStorage.create(review);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании отзыва: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Произошла ошибка при создании отзыва", e);
            throw e;
        }
    }

    public Review findById(Long id) {
        return reviewStorage.findById(id);
    }

    public List<Review> getReviewsByFilmId(int filmId, int count) {
        return reviewStorage.getReviewsByFilmId(filmId, count);
    }

    public void likeReview(Long reviewId, Long userId) {
        reviewStorage.likeReview(reviewId, userId);
    }

    public void dislikeReview(Long reviewId, Long userId) {
        reviewStorage.dislikeReview(reviewId, userId);
    }

    public Collection<Review> findAll() {
        return reviewStorage.findAll();
    }

    public Review update(Review review) {
        if (review.getReviewId() == 0) {
            throw new ValidationException("Отзыв должен иметь идентификатор (reviewId)");
        }
        return reviewStorage.update(review);
    }

    public void delete(long id) {
        reviewStorage.delete(id);
    }

}

