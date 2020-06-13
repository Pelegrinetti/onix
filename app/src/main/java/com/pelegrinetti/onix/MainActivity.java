package com.pelegrinetti.onix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pelegrinetti.onix.database.DB;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> tasks;
    FloatingActionButton createTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadWidgets();

        setEvents();

        createTaskList();
    }

    public void loadWidgets() {
        createTask = (FloatingActionButton) findViewById(R.id.create_task);
    }

    public void setEvents() {
        createTask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private ArrayList<Task> loadTasks() {
        DB db = new DB(MainActivity.this);

        TextView counterTasks = (TextView) findViewById(R.id.count_tasks);

        ArrayList<Task> tasks = db.listTask();

        counterTasks.setText(Integer.toString(tasks.size()));

        return tasks;
    }

    public void createTaskList() {
        LinearLayout tasksContainer = (LinearLayout) findViewById(R.id.tasks_container);

        tasks = loadTasks();

        for (Task task: tasks) {
            LinearLayout taskItems = new LinearLayout(findViewById(R.id.main_linear).getContext());
            taskItems.setOrientation(LinearLayout.HORIZONTAL);
            taskItems.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            );

            LayoutInflater inflater = LayoutInflater.from(this);
            View cardView = inflater.inflate(R.layout.card_item, taskItems, false);

            ViewHolder holder = new ViewHolder(cardView);

            holder.cardTitle.setText(task.getTitle());
            holder.cardDescription.setText(task.getDescription());

            taskItems.addView(cardView);
            tasksContainer.addView(taskItems);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cardTitle;
        public TextView cardDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            cardTitle = (TextView) itemView.findViewById(R.id.card_list_title);
            cardDescription = (TextView) itemView.findViewById(R.id.card_list_description);
        }
    }
}