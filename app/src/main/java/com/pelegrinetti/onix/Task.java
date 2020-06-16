package com.pelegrinetti.onix;

import androidx.annotation.NonNull;

public class Task {
    private int id;
    private String title, description, createdAt, time;

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Task(String title, String description, String createdAt, String time) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTime() {
        return time;
    }
}
