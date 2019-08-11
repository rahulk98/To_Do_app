package com.example.rahulkrishnan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBSQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "tasks_database.db";

    public TaskDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBTask.Tasks.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBTask.Tasks.TABLE_NAME);
        onCreate(db);
    }

    public void markAsDone(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.rawQuery("delete from " + DBTask.Tasks.TABLE_NAME + " WHERE _ID = " + id, null);
    }

    public Cursor getTodayTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + date + "' order by _ID desc", null);
        return res;
    }

    public Cursor getTomorrowTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + date + "' order by _ID desc", null);
        return res;
    }

    public Cursor getUpcomingTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " > '" + date + "' order by _ID desc", null);
        return res;
    }
}
