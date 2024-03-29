package com.example.rahulkrishnan;

import android.provider.BaseColumns;

public final class DBTask {
    private DBTask() {
    }

    public static class Tasks implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TASK_NAME = "task_name";
        public static final String COLUMN_TASK_DATE = "task_date";
        public static final String COLUMN_TASK_TIME = "task_time";
        public static final String COLUMN_TASK_DONE = "task_done";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT, " +
                COLUMN_TASK_DATE + " TEXT, " +
                COLUMN_TASK_TIME + " TEXT, " +
                COLUMN_TASK_DONE + " INTEGER DEFAULT 0" + ")";
    }
}

