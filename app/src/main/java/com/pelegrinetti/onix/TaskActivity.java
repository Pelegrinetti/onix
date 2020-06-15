package com.pelegrinetti.onix;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.airbnb.lottie.LottieAnimationView;
import com.pelegrinetti.onix.database.DB;

import java.util.Objects;

public class TaskActivity extends AppCompatActivity {
    Button btnPickTime, btnSaveTask;
    String pickedTime;
    EditText txtTaskTitle, txtTaskDescription;
    Bundle bundle;
    LottieAnimationView successAnimation;
    RelativeLayout animationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        pickedTime = Objects.requireNonNull(getIntent().getExtras()).getString("date");

        loadWidgets();

        setEvents();
    }

    public void loadWidgets() {
        btnPickTime = (Button) findViewById(R.id.task_time);
        btnSaveTask = (Button) findViewById(R.id.task_save);
        txtTaskTitle = (EditText) findViewById(R.id.task_title);
        txtTaskDescription = (EditText) findViewById(R.id.task_description);
        successAnimation = (LottieAnimationView) findViewById(R.id.success_animation);
        animationContainer = (RelativeLayout) findViewById(R.id.animation_background);
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

        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB db = new DB(TaskActivity.this);

                db.createTask(new Task(
                        txtTaskTitle.getText().toString(),
                        txtTaskDescription.getText().toString(),
                        null,
                        pickedTime
                ));

                animationContainer.setVisibility(View.VISIBLE);

                successAnimation.playAnimation();

                successAnimation.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // nothing for now
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setResult(RESULT_OK, null);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // nothing for now
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // nothing for now
                    }
                });
            }
        });
    }
}