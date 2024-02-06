package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ResourceNotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FeedService {
    private final FeedStorage feedStorage;

    public List<Feed> getFeedsByUserId(long userId) {
        List<Feed> feedList;

        try {
            feedList = feedStorage.getFeedsByUserId(userId);
        } catch (NullPointerException e) {
            return new ArrayList<>() {
            };
        }

        if (feedList.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Не найден пользователь с id = %s", userId));
        }

        return feedList;
    }

    void addFeedRemoveFriend(Long id, Long friendId) {
        feedStorage.addFeedRemoveFriend(id, friendId);
    }

    public void addFeedAddFriend(Long id, Long friendId) {
        feedStorage.addFeedAddFriend(id, friendId);
    }

    public void addFeedAddLike(Long id, Long likeId) {
        feedStorage.addFeedAddLike(id, likeId);
    }

    public void addFeedRemoveLike(Long id, Long likeId) {
        feedStorage.addFeedRemoveLike(id, likeId);
    }

    public void addFeedAddReview(Long id, Long reviewId) {
        feedStorage.addFeedAddReview(id, reviewId);
    }

    public void addFeedRemoveReview(Long id, Long reviewId) {
        feedStorage.addFeedRemoveReview(id, reviewId);
    }

    public void addFeedUpdateReview(Long id, Long reviewId) {
        feedStorage.addFeedUpdateReview(id, reviewId);
    }
}