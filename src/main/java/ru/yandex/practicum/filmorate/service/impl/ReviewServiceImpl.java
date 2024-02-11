package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.service.ReviewService;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewStorage reviewStorage;
    private final FeedService feedService;

    public Review create(Review review) {
        Review reviewNew;
        try {
            reviewNew = reviewStorage.create(review);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании отзыва: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Произошла ошибка при создании отзыва", e);
            throw e;
        }

        feedService.addFeedAddReview((long) reviewNew.getUserId(), reviewNew.getReviewId());
        return reviewNew;
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

    public List<Review> findAll() {
        return reviewStorage.findAll();
    }

    public Review update(Review review) {
        if (review.getReviewId() == 0) {
            throw new ValidationException("Отзыв должен иметь идентификатор");
        }
        Review reviewUpd = reviewStorage.update(review);
        feedService.addFeedUpdateReview((long) reviewUpd.getUserId(), reviewUpd.getReviewId());
        return reviewUpd;
    }

    public void delete(long id) {
        Review review = reviewStorage.findById(id);
        reviewStorage.delete(id);
        feedService.addFeedRemoveReview((long) review.getUserId(), review.getReviewId());
    }
}