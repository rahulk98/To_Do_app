package com.example.rahulkrishnan;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Add_Task_Activity extends AppCompatActivity {
    public static final String LOG_TAGE = Add_Task_Activity.class.getSimpleName();
    final Calendar myCalendar = Calendar.getInstance();
    EditText taskName;
    EditText taskDate;
    public Boolean isUpdate = Boolean.FALSE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__task_);
        taskName = (EditText) findViewById(R.id.task_name);
        taskDate = (EditText) findViewById(R.id.task_date);
        Intent intent = getIntent();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        taskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Task_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
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
        if(nameTask.trim().length() < 2){
            Toast.makeText(this, "Please enter task name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dateTask.trim().length()<2){
            Toast.makeText(this, "Please enter task date", Toast.LENGTH_SHORT).show();
            return;
        }

        if(isUpdate){

        }
        else{
            SQLiteDatabase db = new TaskDBSQLiteHelper(this).getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTask.Tasks.COLUMN_TASK_NAME, nameTask);
            contentValues.put(DBTask.Tasks.COLUMN_TASK_DATE, dateTask);
            long newrowid = new 
        }

    }
}
