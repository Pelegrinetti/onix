package com.pelegrinetti.onix;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Objects;

public class TaskActivity extends AppCompatActivity {
    Button btnPickTime, btnSaveTask;
    String pickedTime;
    EditText txtTaskTitle, txtTaskDescription;
    Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        loadWidgets();

        setEvents();
    }

    public void loadWidgets() {
        btnPickTime = (Button) findViewById(R.id.task_time);
        btnSaveTask = (Button) findViewById(R.id.task_save);
        txtTaskTitle = (EditText) findViewById(R.id.task_title);
        txtTaskDescription = (EditText) findViewById(R.id.task_description);
        bundle = getIntent().getExtras();
    }

    public void setEvents() {
        btnPickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                int hour, minute;

                hour = c.get(Calendar.HOUR);
                minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(TaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String[] date = Objects.requireNonNull(bundle.getString("date")).split(" ");

                        pickedTime = date[0] +
                                " " +
                                (hourOfDay <= 9 ? "0" + String.valueOf(hourOfDay) : String.valueOf(hourOfDay)) +
                                ":" +
                                (minute <= 9 ? "0" + String.valueOf(minute) : String.valueOf(minute)) +
                                ":00";
                    }
                }, hour, minute, true);

                timePickerDialog.show();
            }
        });
    }
}