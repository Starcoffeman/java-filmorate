package ru.yandex.practicum.filmorate.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.service.FeedService;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class FeedServiceImpl implements FeedService {
    private final FeedStorage feedStorage;

    public List<Feed> getFeedsByUserId(long userId) {
        return feedStorage.getFeedsByUserId(userId);
    }

    public void addFeedRemoveFriend(Long id, Long friendId) {
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