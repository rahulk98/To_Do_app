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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static int REQUEST_CODE = 101;
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    private TaskDBSQLiteHelper myDb;

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
        if(item.getItemId() == R.id.about_dev){
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

    private void populateDB() {
        Calendar calendar = Calendar.getInstance();
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Cursor todayTasks = myDb.getTodayTasks(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1); //Increment date to get tasks from tomorrow
        Cursor tomorrowTasks = myDb.getTomorrowTasks(sdf.format(calendar.getTime()));
        Cursor upcomingTasks = myDb.getUpcomingTasks(sdf.format(calendar.getTime()));
        TaskListAdapter todayListAdapter = new TaskListAdapter(todayTasks, MainActivity.this);
        TaskListAdapter tomorrowListAdapter = new TaskListAdapter(tomorrowTasks, MainActivity.this);
        TaskListAdapter upcomingListAdapter = new TaskListAdapter(upcomingTasks, MainActivity.this);

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
}
