package com.example.rahulkrishnan;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TaskClickListener {
    public static int REQUEST_CODE = 101;
    private TaskDBSQLiteHelper myDb;
    public static String UPDATE_TAG = "isUpdate";
    public static String TASK_NAME_TAG = "taskName";
    public static String TASK_DATE_TAG = "taskDate";
    public static String TASK_TIME_TAG = "taskTime";
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
        ItemTouchHelper todayhelper = new ItemTouchHelper(
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
                        todayTasks.moveToPosition(position);
                        String taskName = todayTasks.getString(1);
                        String taskDate = todayTasks.getString(2);
                        myDb.deleteTask(taskName, taskDate);
                        populateDB();
                    }
                });

        todayhelper.attachToRecyclerView(todayTaskList);
        ItemTouchHelper tomorrowhelper = new ItemTouchHelper(
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
                        tomorrowTasks.moveToPosition(position);
                        String taskName = tomorrowTasks.getString(1);
                        String taskDate = tomorrowTasks.getString(2);
                        myDb.deleteTask(taskName, taskDate);
                        populateDB();
                    }
                });

        tomorrowhelper.attachToRecyclerView(tomorrowTaskList);
        ItemTouchHelper upcominghelper = new ItemTouchHelper(
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
                        upcomingTasks.moveToPosition(position);
                        String taskName = upcomingTasks.getString(1);
                        String taskDate = upcomingTasks.getString(2);
                        myDb.deleteTask(taskName, taskDate);
                        populateDB();
                    }
                });

        upcominghelper.attachToRecyclerView(upcomingTaskList);
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
                findViewById(R.id.today_tasks_empty_TV).setVisibility(View.GONE);
                findViewById(R.id.tomorrow_tasks_empty_TV).setVisibility(View.GONE);
                findViewById(R.id.upcoming_tasks_empty_TV).setVisibility(View.GONE);
                populateDB();
            }
        }
    }

    public void populateDB() {
        Calendar calendar = Calendar.getInstance();
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        todayTasks = myDb.getTodayTasks(sdf.format(calendar.getTime()));
        calendar.add(Calendar.DAY_OF_MONTH, 1); //Increment date to get tasks from tomorrow
        tomorrowTasks = myDb.getTomorrowTasks(sdf.format(calendar.getTime()));
        upcomingTasks = myDb.getUpcomingTasks(sdf.format(calendar.getTime()));
        TaskListAdapter todayListAdapter = new TaskListAdapter(todayTasks, this, "today");
        TaskListAdapter tomorrowListAdapter = new TaskListAdapter(tomorrowTasks, this, "tomorrow");
        TaskListAdapter upcomingListAdapter = new TaskListAdapter(upcomingTasks, this, "upcoming");
        if (todayTasks.getCount() == 0) {
            findViewById(R.id.today_tasks_empty_TV).setVisibility(View.VISIBLE);
        }
        if (tomorrowTasks.getCount() == 0) {
            findViewById(R.id.tomorrow_tasks_empty_TV).setVisibility(View.VISIBLE);
        }
        if (upcomingTasks.getCount() == 0) {
            findViewById(R.id.upcoming_tasks_empty_TV).setVisibility(View.VISIBLE);
        }
        todayTaskList.setLayoutManager(new LinearLayoutManager(this));
        todayTaskList.setAdapter(todayListAdapter);
        todayTaskList.setNestedScrollingEnabled(false);
        tomorrowTaskList.setLayoutManager(new LinearLayoutManager(this));
        tomorrowTaskList.setAdapter(tomorrowListAdapter);
        tomorrowTaskList.setNestedScrollingEnabled(false);
        upcomingTaskList.setLayoutManager(new LinearLayoutManager(this));
        upcomingTaskList.setAdapter(upcomingListAdapter);
        upcomingTaskList.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateDB();
    }

    @Override
    public void onItemClick(View v, int position, String listType) {
        String taskName = "", taskDate = "", taskTime = "";
        if (listType.equals("today")) {
            if (todayTasks != null) {
                todayTasks.moveToPosition(position);
                taskName = todayTasks.getString(1);
                taskDate = todayTasks.getString(2);
                taskTime = todayTasks.getString(3);
            }
        }
        if (listType.equals("tomorrow")) {
            if (tomorrowTasks != null) {
                tomorrowTasks.moveToPosition(position);
                taskName = tomorrowTasks.getString(1);
                taskDate = tomorrowTasks.getString(2);
                taskTime = tomorrowTasks.getString(3);
            }
        }
        if (listType.equals("upcoming")) {
            if (upcomingTasks != null) {
                upcomingTasks.moveToPosition(position);
                taskName = upcomingTasks.getString(1);
                taskDate = upcomingTasks.getString(2);
                taskTime = upcomingTasks.getString(3);
            }
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final String finalTaskName = taskName;
        final String finalTaskDate = taskDate;
        final String finalTaskTime = taskTime;
        alert.setPositiveButton("Mark Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = myDb.getID(finalTaskName, finalTaskDate);
                myDb.markDone(id);
                populateDB();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.setNeutralButton("Edit Task", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, Add_Task_Activity.class);
                intent.putExtra(TASK_NAME_TAG, finalTaskName);
                intent.putExtra(TASK_DATE_TAG, finalTaskDate);
                intent.putExtra(TASK_TIME_TAG, finalTaskTime);
                intent.putExtra(UPDATE_TAG, true);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        alert.setTitle("Task");
        alert.setMessage(taskName + "\nDue on " + taskDate + " " + taskTime);
        alert.create();
        alert.show();

    }

    public void openHistory(MenuItem item) {
        Intent intent = new Intent(this, TaskHistory.class);
        startActivity(intent);

    }
}
