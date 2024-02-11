package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import javax.validation.constraints.Positive;
import java.util.Map;

@Data
@Builder
public class Feed {
    @Positive
    private long eventId;
    @Positive
    private long timestamp;
    @Positive
    private long userId;
    @NonNull
    private EventType eventType;
    @NonNull
    private Operation operation;
    @Positive
    private long entityId;

    public Map<String, Object> toMapForDB() {
        return Map.of("TIMESTAMP", timestamp, "USER_ID", userId, "EVENT_TYPE", eventType,
                "OPERATION", operation, "ENTITY_ID", entityId);
    }
}