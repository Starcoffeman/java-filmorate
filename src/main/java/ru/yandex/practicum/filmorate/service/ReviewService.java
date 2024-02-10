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
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final FeedService feedService;

    public Review create(Review review) {
        Review reviewNew;
        try {
            reviewNew = reviewStorage.create(review);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
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

    public Collection<Review> findAll() {
        return reviewStorage.findAll();
    }

    public Review update(Review review) {
        if (review.getReviewId() == 0) {
            throw new ValidationException("Отзыв должен иметь идентификатор (reviewId)");
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