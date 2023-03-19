package com.reactive.database.r2dbcpostgresqltodolist.exception;

import com.reactive.database.r2dbcpostgresqltodolist.db.entity.NotificationTopic;

public class NotificationDeserializationException extends RuntimeException {

    public NotificationDeserializationException(NotificationTopic topic, Throwable cause) {
        super(String.format("Cannot deserialize the notification for topic [%s]", topic), cause);
    }

}
