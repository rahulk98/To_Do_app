package com.example.rahulkrishnan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class TaskHistory extends AppCompatActivity implements TaskClickListener{
    RecyclerView taskHistoryList;
    TaskListAdapter historyList;
    private TaskDBSQLiteHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        taskHistoryList = findViewById(R.id.history_recyclerview);
        populateDB();
    }

    private void populateDB() {
        myDb = new TaskDBSQLiteHelper(this);
        historyList = new TaskListAdapter(myDb.getDoneTasks(), this, getString(R.string.history_label));
        taskHistoryList.setLayoutManager(new LinearLayoutManager(this));
        taskHistoryList.setAdapter(historyList);
    }

    @Override
    public void onItemClick(View v, int position, String listType) {

    }
}
