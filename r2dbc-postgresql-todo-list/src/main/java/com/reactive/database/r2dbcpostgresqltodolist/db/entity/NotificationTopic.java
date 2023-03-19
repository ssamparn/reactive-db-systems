package com.reactive.database.r2dbcpostgresqltodolist.db.entity;

import lombok.Getter;

/**
 * PostgreSQL notification topics
 */
@Getter
public enum NotificationTopic {

    ITEM_SAVED,
    ITEM_DELETED
}
