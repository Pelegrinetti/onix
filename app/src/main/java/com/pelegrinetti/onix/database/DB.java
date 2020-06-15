package com.pelegrinetti.onix.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pelegrinetti.onix.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + "(" +
                    TaskContract.TaskEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TaskContract.TaskEntry.COLUMN_NAME_TITLE + " TEXT, " +
                    TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION + " TEXT, " +
                    TaskContract.TaskEntry.COLUMN_NAME_TIME + " DATETIME, " +
                    TaskContract.TaskEntry.COLUMN_NAME_CREATE_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME + ";";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "onix";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void createTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TaskContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TaskContract.TaskEntry.COLUMN_NAME_TIME, task.getTime());

        db.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Task> listTask(String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] dateHour = date.split(" ");

        String[] projection = {
                TaskContract.TaskEntry.COLUMN_NAME_ID,
                TaskContract.TaskEntry.COLUMN_NAME_TITLE,
                TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_NAME_TIME
        };

        String selection = TaskContract.TaskEntry.COLUMN_NAME_TIME + " BETWEEN ? AND ?";
        String[] selectionArgs = { dateHour[0] + " 00:00:00", dateHour[0] + " 23:59:59" };

        String sortOrder = TaskContract.TaskEntry.COLUMN_NAME_CREATE_AT + " ASC";

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        ArrayList<Task> items = new ArrayList<>();

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_ID)
            );
            String itemTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TITLE)
            );
            String itemDescription = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_DESCRIPTION)
            );
            String itemTime = cursor.getString(
                    cursor.getColumnIndexOrThrow(TaskContract.TaskEntry.COLUMN_NAME_TIME)
            );

            items.add(new Task(itemTitle, itemDescription, null, itemTime));
        }

        cursor.close();
        db.close();

        return items;
    }
}
