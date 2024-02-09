package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.enums.EventType;
import ru.yandex.practicum.filmorate.model.enums.Operation;

import java.util.Map;

@Data
@Builder
public class Feed {
    private long eventId;
    private long timestamp;
    private long userId;
    private EventType eventType;
    private Operation operation;
    private long entityId;

    public Map<String, Object> toMapForDB() {
        return Map.of("TIMESTAMP", timestamp, "USER_ID", userId, "EVENT_TYPE", eventType,
                "OPERATION", operation, "ENTITY_ID", entityId);
    }
}