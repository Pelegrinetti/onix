package com.pelegrinetti.onix.database;

import android.provider.BaseColumns;

public class TaskContract {
    private TaskContract() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_CREATE_AT = "created_at";
    }
}
