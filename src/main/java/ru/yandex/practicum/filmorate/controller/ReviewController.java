package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public Review add(@Valid @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        return reviewService.update(review);
    }

    @GetMapping
    public Collection<Review> getAll(@RequestParam(name = "filmId", required = false) Integer filmId,
                                     @RequestParam(name = "count", defaultValue = "10") Integer count) {
        if (filmId == null) {
            return reviewService.findAll();
        } else {
            return reviewService.getReviewsByFilmId(filmId, count);
        }
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable("reviewId") Long id) {
        reviewService.delete(id);

    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void likeReview(
            @PathVariable("reviewId") Long reviewId,
            @PathVariable("userId") Long userId
    ) {
        reviewService.likeReview(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void dislikeReview(
            @PathVariable("reviewId") Long reviewId,
            @PathVariable("userId") Long userId
    ) {
        reviewService.dislikeReview(reviewId, userId);
    }


    @GetMapping("/{reviewId}")
    public Review findById(@PathVariable("reviewId") Long id) {
        return reviewService.findById(id);
    }
}
