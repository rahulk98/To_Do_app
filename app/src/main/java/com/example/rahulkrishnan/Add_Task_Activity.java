package com.example.rahulkrishnan;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_Task_Activity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    public Boolean isUpdate;
    EditText taskName;
    EditText taskDate;
    EditText taskTime;
    String oldTaskName;
    String oldTaskDate;
    TaskDBSQLiteHelper taskDBSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__task_);
        taskName = findViewById(R.id.task_name);
        taskDate = findViewById(R.id.task_date);
        Intent intent = getIntent();
        taskTime = findViewById(R.id.task_time);
        isUpdate = intent.getBooleanExtra(MainActivity.UPDATE_TAG, false);
        taskDBSQLiteHelper = new TaskDBSQLiteHelper(this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        if (isUpdate) {
            ((TextView) findViewById(R.id.add_task_header)).setText(this.getString(R.string.edit_task_header));
            invalidateOptionsMenu();
            oldTaskDate = intent.getStringExtra(MainActivity.TASK_DATE_TAG);
            oldTaskName = intent.getStringExtra(MainActivity.TASK_NAME_TAG);
            String oldTaskTime = intent.getStringExtra(MainActivity.TASK_TIME_TAG);
            taskDate.setText(oldTaskDate);
            taskName.setText(oldTaskName);
            taskTime.setText(oldTaskTime);
            final String dateValues[] = oldTaskDate.split("/");
            taskDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(Add_Task_Activity.this, date, Integer.parseInt(dateValues[2]), Integer.parseInt(dateValues[0]) - 1,
                            Integer.parseInt(dateValues[1])).show();
                }
            });
            final String temp[] = oldTaskTime.split(":");
            taskTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Add_Task_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            taskTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });

        } else {
            taskDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(Add_Task_Activity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            taskTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Add_Task_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            taskTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
            });
        }


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        taskDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    public void addTask(MenuItem item) {
        String nameTask = taskName.getText().toString();
        String dateTask = taskDate.getText().toString();
        String timeTask = taskTime.getText().toString();
        if (nameTask.trim().length() < 2) {
            Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dateTask.trim().length() < 2) {
            Toast.makeText(this, "Please enter task date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (timeTask.trim().length() < 2) {
            Toast.makeText(this, "Please enter task time", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase db = new TaskDBSQLiteHelper(this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBTask.Tasks.COLUMN_TASK_NAME, nameTask);
        contentValues.put(DBTask.Tasks.COLUMN_TASK_DATE, dateTask);
        contentValues.put(DBTask.Tasks.COLUMN_TASK_TIME, timeTask);
        if (isUpdate) {
            String id = taskDBSQLiteHelper.getID(oldTaskName, oldTaskDate);
            db.update(DBTask.Tasks.TABLE_NAME, contentValues, "_ID = ? ", new String[]{id});

        } else {

            long row = db.insert(DBTask.Tasks.TABLE_NAME, null, contentValues);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isUpdate) {
            MenuItem deleteBTN = menu.findItem(R.id.delete_task);
            deleteBTN.setVisible(true);
        }
        return true;
    }

    public void deleteTask(MenuItem item) {
        taskDBSQLiteHelper.deleteTask(oldTaskName, oldTaskDate);
        Toast.makeText(this, "Deleted task", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
