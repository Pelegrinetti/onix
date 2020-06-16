package com.pelegrinetti.onix;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
    DB db;
    Task task;
    int taskId;

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

        setEvents();
    }

    private void loadWidgets() {
        btnFinishTask = (Button) findViewById(R.id.finish_task);
        btnDeleteTask = (Button) findViewById(R.id.delete_task);
        tvTaskTitle = (TextView) findViewById(R.id.task_title);
        tvTaskDescription = (TextView) findViewById(R.id.task_description);
        tvTaskCreatedAt = (TextView) findViewById(R.id.created_at);
        tvTaskTime = (TextView) findViewById(R.id.task_time);

        bundle = getIntent().getExtras();
        assert bundle != null;
        taskId = bundle.getInt("taskId");
        db = new DB(this);
        task = db.readTask(taskId);
    }

    @SuppressLint("SetTextI18n")
    public void setTaskInfo() throws ParseException {
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

    private void setEvents() {
        btnDeleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReadTaskActivity.this);

                builder.setTitle("Tem certeza?");
                builder.setMessage("Não será possível recuperar esta tarefa...");

                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean result = db.deleteTask(taskId);

                        if (result) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}