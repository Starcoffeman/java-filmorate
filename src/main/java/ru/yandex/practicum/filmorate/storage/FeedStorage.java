package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Feed;

import java.util.List;

public interface FeedStorage {
    List<Feed> getFeedsByUserId(long userId);

    void addFeedAddFriend(Long id, Long friendId);

    void addFeedRemoveFriend(Long id, Long friendId);

    void addFeedAddLike(Long id, Long likeId);

    void addFeedRemoveLike(Long id, Long likeId);

    void addFeedAddReview(Long id, Long reviewId);

    void addFeedRemoveReview(Long id, Long reviewId);

    void addFeedUpdateReview(Long id, Long reviewId);
}