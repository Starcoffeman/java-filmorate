package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review create(@Valid @RequestBody Review review) {
        log.info("Отзыв создан review {}", review);
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        log.info("Отзыв обновлён review {}", review);
        return reviewService.update(review);
    }

    @GetMapping
    public Collection<Review> findAll(@RequestParam(name = "filmId", required = false) Integer filmId,
                                      @RequestParam(name = "count", defaultValue = "10") Integer count) {
        if (filmId == null) {
            log.info("Вывод всех отзывов");
            return reviewService.findAll();
        } else {
            log.info("Вывод всех отзывов у фильма под filmId {}", filmId);
            return reviewService.getReviewsByFilmId(filmId, count);
        }
    }

    @DeleteMapping("/{reviewId}")
    public void delete(@PathVariable("reviewId") Long id) {
        log.info("Отзыв под id {} удалён", id);
        reviewService.delete(id);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void likeReview(@PathVariable("reviewId") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Отзыв под reviewId {} был лайкнут пользователем userId {}", reviewId, userId);
        reviewService.likeReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void dislikeReview(@PathVariable("reviewId") Long reviewId, @PathVariable("userId") Long userId) {
        log.info("Отзыв под reviewId {} был дислайкнут пользователем userId {}", reviewId, userId);
        reviewService.dislikeReview(reviewId, userId);
    }


    @GetMapping("/{reviewId}")
    public Review findById(@PathVariable("reviewId") Long id) {
        log.info("Вывод отзыва под id {}", id);
        return reviewService.findById(id);
    }
}