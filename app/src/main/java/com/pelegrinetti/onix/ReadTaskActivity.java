package com.pelegrinetti.onix;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.pelegrinetti.onix.database.DB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadTaskActivity extends AppCompatActivity {
    Button btnFinishTask, btnDeleteTask;
    TextView tvTaskTitle, tvTaskDescription, tvTaskCreatedAt, tvTaskTime;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_task);

        loadWidgets();

        try {
            setTaskInfo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadWidgets() {
        btnFinishTask = (Button) findViewById(R.id.finish_task);
        btnDeleteTask = (Button) findViewById(R.id.delete_task);
        tvTaskTitle = (TextView) findViewById(R.id.task_title);
        tvTaskDescription = (TextView) findViewById(R.id.task_description);
        tvTaskCreatedAt = (TextView) findViewById(R.id.created_at);
        tvTaskTime = (TextView) findViewById(R.id.task_time);

        bundle = getIntent().getExtras();
    }

    @SuppressLint("SetTextI18n")
    public void setTaskInfo() throws ParseException {
        int taskId = bundle.getInt("taskId");

        DB db = new DB(this);

        Task task = db.readTask(taskId);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date createdAt = defaultFormat.parse(task.getCreatedAt());
        Date forDate = defaultFormat.parse(task.getTime());

        tvTaskTitle.setText(task.getTitle());
        tvTaskDescription.setText(task.getDescription());
        assert createdAt != null;
        tvTaskCreatedAt.setText(getResources().getString(R.string.created_at) + " " + formatter.format(createdAt));
        assert forDate != null;
        tvTaskTime.setText(getResources().getString(R.string.to) + " " + formatter.format(forDate));
    }
}