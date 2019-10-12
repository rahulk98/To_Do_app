package com.example.rahulkrishnan;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TaskHistory extends AppCompatActivity implements TaskClickListener {
    RecyclerView taskFinishedList;
    TaskListAdapter finishedList;
    private TaskDBSQLiteHelper myDb;
    RecyclerView taskOverDueList;
    TaskListAdapter overDueList;
    Cursor historyTaskList, overDueTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        taskFinishedList = findViewById(R.id.history_recyclerview);
        taskOverDueList = findViewById(R.id.unfinished_recyclerview);
        myDb = new TaskDBSQLiteHelper(this);

        ItemTouchHelper finishedTaskHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        historyTaskList.moveToPosition(position);
                        String taskName = historyTaskList.getString(1);
                        String taskDate = historyTaskList.getString(2);
                        myDb.deleteTask(taskName, taskDate);
                        populateDB();
                    }
                });

        finishedTaskHelper.attachToRecyclerView(taskFinishedList);
        ItemTouchHelper overDueTaskHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Cursor temp = overDueTaskList;
                        overDueTaskList.moveToPosition(position);
                        String taskName = overDueTaskList.getString(1);
                        String taskDate = overDueTaskList.getString(2);
                        myDb.deleteTask(taskName, taskDate);
                        populateDB();
                    }
                });

        overDueTaskHelper.attachToRecyclerView(taskOverDueList);
        populateDB();
    }

    private void populateDB() {
        historyTaskList = myDb.getDoneTasks();
        Calendar calendar = Calendar.getInstance();
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        overDueTaskList = myDb.getOverDueTaks(sdf.format(calendar.getTime()));
        finishedList = new TaskListAdapter(historyTaskList, this, getString(R.string.history_label));
        taskFinishedList.setLayoutManager(new LinearLayoutManager(this));
        taskFinishedList.setAdapter(finishedList);
        taskFinishedList.setNestedScrollingEnabled(false);
        overDueList = new TaskListAdapter(overDueTaskList, this, getString(R.string.overdue_label));
        taskOverDueList.setLayoutManager(new LinearLayoutManager(this));
        taskOverDueList.setAdapter(overDueList);
        taskOverDueList.setNestedScrollingEnabled(false);

    }

    @Override
    public void onItemClick(View v, int position, String listType) {

    }
}
