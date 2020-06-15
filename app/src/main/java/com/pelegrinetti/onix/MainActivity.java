package com.pelegrinetti.onix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pelegrinetti.onix.database.DB;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> tasks;
    FloatingActionButton createTask;
    CalendarView calendar;
    String pickedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadWidgets();

        long actualDate = calendar.getDate();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pickedDate = formatter.format(actualDate);

        setEvents();

        createTaskList();
    }

    public void loadWidgets() {
        createTask = (FloatingActionButton) findViewById(R.id.create_task);
        calendar = (CalendarView) findViewById(R.id.calendar);
    }

    public void setEvents() {
        createTask.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TaskActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("date", pickedDate);

                intent.putExtras(bundle);

                startActivityForResult(intent, 0);
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String parsedYear, parsedMonth, parsedDayOfMonth;

                parsedYear = String.valueOf(year);
                parsedMonth = String.valueOf(month + 1);
                parsedDayOfMonth = String.valueOf(dayOfMonth);

                pickedDate = parsedYear +
                        "-" +
                        (month <= 9 ? "0" + parsedMonth : parsedMonth) +
                        "-" +
                        (dayOfMonth <= 9 ? "0" + parsedDayOfMonth : parsedDayOfMonth) +
                        " 00:00:00";

                createTaskList();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void createTaskList() {
        LinearLayout tasksContainer = (LinearLayout) findViewById(R.id.tasks_container);
        TextView counterTasks = (TextView) findViewById(R.id.count_tasks);

        tasksContainer.removeAllViews();

        DB db = new DB(MainActivity.this);

        tasks = db.listTask(pickedDate);

        if (tasks.size() > 0) {
            findViewById(R.id.no_tasks).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_tasks).setVisibility(View.VISIBLE);
        }

        counterTasks.setText(Integer.toString(tasks.size()));

        for (Task task : tasks) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Intent refresh = new Intent(this, MainActivity.class);

            startActivity(refresh);

            this.finish();
        }
    }
}