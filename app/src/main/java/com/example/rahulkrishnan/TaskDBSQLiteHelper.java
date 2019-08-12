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

    //TODO fix delete from db
    public void deleteTask(String taskName, String taskDate) {
        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery("DELETE from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_NAME + " = '" + taskName + "' AND " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + taskDate + "'", null);
//        Log.d("query", cursor.getCount()+"");
        db.delete(DBTask.Tasks.TABLE_NAME, DBTask.Tasks.COLUMN_TASK_DATE + " = ? AND " + DBTask.Tasks.COLUMN_TASK_NAME + " = ?", new String[]{taskDate, taskName});
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

    public String getID(String taskName, String taskDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select _ID from " + DBTask.Tasks.TABLE_NAME + " WHERE " + DBTask.Tasks.COLUMN_TASK_NAME + " = '" + taskName + "' AND " + DBTask.Tasks.COLUMN_TASK_DATE + " = '" + taskDate + "'", null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }
}
