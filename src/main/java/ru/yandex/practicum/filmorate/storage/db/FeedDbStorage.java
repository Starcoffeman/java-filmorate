package ru.yandex.practicum.filmorate.storage.db;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Repository
@AllArgsConstructor
public class FeedDbStorage implements FeedStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Feed> getFeedsByUserId(long userId) {
        return jdbcTemplate.query("SELECT U.ID,  F.* " +
                        "FROM USERS U " +
                        "LEFT JOIN FEED F ON U.ID = F.USER_ID " +
                        "WHERE U.ID = ? " +
                        "ORDER BY TIMESTAMP",
                this::mapRowToFeed,
                userId);
    }

    @Override
    public void addFeedAddFriend(Long id, Long friendId) {
        addFeedGeneralized(id, friendId, EventType.FRIEND, Operation.ADD);
    }

    @Override
    public void addFeedRemoveFriend(Long id, Long friendId) {
        addFeedGeneralized(id, friendId, EventType.FRIEND, Operation.REMOVE);
    }

    @Override
    public void addFeedAddLike(Long id, Long likeId) {
        addFeedGeneralized(id, likeId, EventType.LIKE, Operation.ADD);
    }

    @Override
    public void addFeedRemoveLike(Long id, Long likeId) {
        addFeedGeneralized(id, likeId, EventType.LIKE, Operation.REMOVE);
    }

    @Override
    public void addFeedAddReview(Long id, Long reviewId) {
        addFeedGeneralized(id, reviewId, EventType.REVIEW, Operation.ADD);
    }

    @Override
    public void addFeedRemoveReview(Long id, Long reviewId) {
        addFeedGeneralized(id, reviewId, EventType.REVIEW, Operation.REMOVE);
    }

    @Override
    public void addFeedUpdateReview(Long id, Long reviewId) {
        addFeedGeneralized(id, reviewId, EventType.REVIEW, Operation.UPDATE);
    }

    private Feed mapRowToFeed(ResultSet rs, int rowNum) throws SQLException {
        return Feed.builder()
                .eventId(rs.getInt("EVENT_ID"))
                .timestamp(rs.getLong("TIMESTAMP"))
                .userId(rs.getInt("USER_ID"))
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(Operation.valueOf(rs.getString("OPERATION")))
                .entityId(rs.getInt("ENTITY_ID"))
                .build();
    }

    private void addFeedGeneralized(long userId, long entityId, EventType eventType, Operation operation) {
        SimpleJdbcInsert simpleJdbcInsert =
                new SimpleJdbcInsert(Objects.requireNonNull(jdbcTemplate.getDataSource()))
                        .withTableName("FEED").usingGeneratedKeyColumns("EVENT_ID");

        Feed feed = Feed.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();

        simpleJdbcInsert.executeAndReturnKey(feed.toMapForDB()).longValue();
    }
}