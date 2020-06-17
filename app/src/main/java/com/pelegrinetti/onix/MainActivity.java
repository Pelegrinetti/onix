package com.pelegrinetti.onix;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
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

        getOrSetUsername();
        loadWidgets();

        long actualDate = calendar.getDate();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        pickedDate = formatter.format(actualDate);

        setEvents();

        createTaskList();
    }

    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    public void getOrSetUsername() {
        final SharedPreferences preferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        String username = preferences.getString("username", "");

        if (username.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText txtUsername = new EditText(this);

            LinearLayout ln = new LinearLayout(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            ln.setLayoutParams(lp);
            lp.setMargins(25, 25, 25, 25);

            txtUsername.setLayoutParams(lp);
            txtUsername.setBackground(getDrawable(R.drawable.edittext));
            txtUsername.setPadding(25, 25, 25, 25);

            ln.addView(txtUsername);

            builder.setTitle("Como quer ser chamado?");
            builder.setMessage("Informe seu nome...");
            builder.setView(ln);

            builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAndRemoveTask();
                }
            });

            builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String username = txtUsername.getText().toString();
                    SharedPreferences.Editor editor = getSharedPreferences("UserPreferences", MODE_PRIVATE).edit();
                    editor.putString("username", username);
                    editor.apply();

                    TextView tvUsername = (TextView) findViewById(R.id.username);
                    tvUsername.setText("Olá, " + username + "!");
                }
            });

            builder.show();
        } else {
            TextView tvUsername = (TextView) findViewById(R.id.username);
            tvUsername.setText("Olá, " + username + "!");
        }
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
        TextView counterFinished = (TextView) findViewById(R.id.count_completed);

        tasksContainer.removeAllViews();

        DB db = new DB(MainActivity.this);

        tasks = db.listTask(pickedDate);

        if (tasks.size() > 0) {
            findViewById(R.id.no_tasks).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_tasks).setVisibility(View.VISIBLE);
        }

        counterTasks.setText(Integer.toString(tasks.size()));

        int countFinished = 0;

        for (Task task : tasks) {
            LinearLayout taskItems = new LinearLayout(findViewById(R.id.main_linear).getContext());
            taskItems.setOrientation(LinearLayout.HORIZONTAL);
            taskItems.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            );

            LayoutInflater inflater = LayoutInflater.from(this);
            View cardView = inflater.inflate(R.layout.card_item, taskItems, false);

            ViewHolder holder = new ViewHolder(cardView);

            String dateTime = task.getTime();

            String[] splitDateTime = dateTime.split(" ");

            String[] time = splitDateTime[1].split(":");

            Log.i("**TASK***", task.toString());

            holder.id = task.getId();
            holder.cardTitle.setText(task.getTitle());
            holder.cardDescription.setText(task.getDescription());
            holder.cardTime.setText(time[0] + ":" + time[1]);

            if (task.isFinished()) {
                countFinished++;
                holder.cardFinished.setVisibility(View.VISIBLE);
            }

            taskItems.addView(cardView);
            tasksContainer.addView(taskItems);
        }

        counterFinished.setText(String.valueOf(countFinished));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int id;
        public TextView cardTitle;
        public TextView cardDescription;
        public TextView cardTime;
        public ImageView cardFinished;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReadTaskActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("taskId", id);

                    intent.putExtras(bundle);

                    startActivityForResult(intent, 0);
                }
            });

            cardTitle = (TextView) itemView.findViewById(R.id.card_list_title);
            cardDescription = (TextView) itemView.findViewById(R.id.card_list_description);
            cardTime = (TextView) itemView.findViewById(R.id.card_list_time);
            cardFinished = (ImageView) itemView.findViewById(R.id.completed_effect);
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