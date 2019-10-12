package com.example.rahulkrishnan;

import android.content.ContentValues;
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

    public void deleteTask(String taskName, String taskDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBTask.Tasks.TABLE_NAME, DBTask.Tasks.COLUMN_TASK_DATE + " = ? AND " + DBTask.Tasks.COLUMN_TASK_NAME + " = ?", new String[]{taskDate, taskName});
    }

    public Cursor getTodayTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + date + "' and " + DBTask.Tasks.COLUMN_TASK_DONE + " == 0  order by _ID desc", null);
        return res;
    }

    public Cursor getTomorrowTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + date + "' and " + DBTask.Tasks.COLUMN_TASK_DONE + " == 0 order by _ID desc", null);
        return res;
    }

    public Cursor getUpcomingTasks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " > '" + date + "' and " + DBTask.Tasks.COLUMN_TASK_DONE + " == 0  order by _ID desc", null);
        return res;
    }

    public Cursor getDoneTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DONE + " == 1 order by _ID desc", null);
        return res;
    }

    public String getID(String taskName, String taskDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select _ID from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_NAME + " = '" + taskName + "' AND " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + taskDate + "'", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    public void markDone(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBTask.Tasks.COLUMN_TASK_DONE, 1);
        db.update(DBTask.Tasks.TABLE_NAME, cv, "_ID = ? ", new String[]{id});
    }

    public Cursor getOverDueTaks(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_DATE + " < '" + date + "' and " + DBTask.Tasks.COLUMN_TASK_DONE + " == 0  order by _ID desc", null);
        return res;
    }
}
