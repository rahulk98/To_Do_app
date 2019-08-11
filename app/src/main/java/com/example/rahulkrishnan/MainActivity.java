package com.example.rahulkrishnan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TaskClickListener {
    public static int REQUEST_CODE = 101;
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    private TaskDBSQLiteHelper myDb;
    public static String UPDATE_TAG = "isUpdate";
    public static String TASK_NAME_TAG = "taskName";
    public static String TASK_DATE_TAG = "taskDate";
    Cursor todayTasks, tomorrowTasks, upcomingTasks;
    private RecyclerView todayTaskList, tomorrowTaskList, upcomingTaskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        todayTaskList = findViewById(R.id.taskListToday);
        tomorrowTaskList = findViewById(R.id.taskListTomorrow);
        upcomingTaskList = findViewById(R.id.taskListUpcoming);
        myDb = new TaskDBSQLiteHelper(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Task_Activity.class);
                intent.putExtra(UPDATE_TAG, false);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        populateDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_menu, menu);
        return true;
    }

    public void openResume(MenuItem item) {
        if (item.getItemId() == R.id.about_dev) {
            Intent intent = new Intent(this, ResumeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                populateDB();
            }
        }
    }

    public void populateDB() {
        Calendar calendar = Calendar.getInstance();
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        todayTasks = myDb.getTodayTasks(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1); //Increment date to get tasks from tomorrow
        tomorrowTasks = myDb.getTomorrowTasks(sdf.format(calendar.getTime()));
        upcomingTasks = myDb.getUpcomingTasks(sdf.format(calendar.getTime()));
        TaskListAdapter todayListAdapter = new TaskListAdapter(todayTasks, getApplicationContext(), this, "today");
        TaskListAdapter tomorrowListAdapter = new TaskListAdapter(tomorrowTasks, getApplicationContext(), this, "tomorrow");
        TaskListAdapter upcomingListAdapter = new TaskListAdapter(upcomingTasks, getApplicationContext(), this, "upcoming");

        todayTaskList.setHasFixedSize(true);
        todayTaskList.setLayoutManager(new LinearLayoutManager(this));
        todayTaskList.setAdapter(todayListAdapter);
        tomorrowTaskList.setHasFixedSize(true);
        tomorrowTaskList.setLayoutManager(new LinearLayoutManager(this));
        tomorrowTaskList.setAdapter(tomorrowListAdapter);

        upcomingTaskList.setHasFixedSize(true);
        upcomingTaskList.setLayoutManager(new LinearLayoutManager(this));
        upcomingTaskList.setAdapter(upcomingListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDB();
    }

    @Override
    public void onItemClick(View v, int position, String listType) {
        String taskName = "", taskDate = "";
        int count = 0;
        if (listType.equals("today")) {
            if (todayTasks != null) {
                todayTasks.moveToFirst();
                while (!todayTasks.isAfterLast()) {
                    if (count == position) {
                        taskName = todayTasks.getString(1);
                        taskDate = todayTasks.getString(2);
                        break;
                    }
                    count++;
                    todayTasks.moveToNext();
                }
            }
        }
        count = 0;
        if (listType.equals("tomorrow")) {
            if (tomorrowTasks != null) {
                tomorrowTasks.moveToFirst();
                while (!tomorrowTasks.isAfterLast()) {

                    if (count == position) {
                        taskName = tomorrowTasks.getString(1);
                        taskDate = tomorrowTasks.getString(2);
                        break;
                    }
                    count++;
                    tomorrowTasks.moveToNext();
                }
            }
        }
        count = 0;
        if (listType.equals("upcoming")) {
            if (upcomingTasks != null) {
                upcomingTasks.moveToFirst();
                while (!upcomingTasks.isAfterLast()) {

                    if (count == position) {
                        taskName = upcomingTasks.getString(1);
                        taskDate = upcomingTasks.getString(2);
                        break;
                    }
                    count++;
                    upcomingTasks.moveToNext();
                }
            }
        }
        Intent intent = new Intent(this, Add_Task_Activity.class);
        intent.putExtra(TASK_NAME_TAG, taskName);
        intent.putExtra(TASK_DATE_TAG, taskDate);
        intent.putExtra(UPDATE_TAG, true);
        Log.d(LOG_TAG, taskDate);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
