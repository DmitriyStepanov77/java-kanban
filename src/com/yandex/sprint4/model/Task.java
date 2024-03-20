package com.yandex.sprint4.model;

import java.util.Objects;

public class Task {
    private static int countTaskAllTime = 0;
    private String name;
    private int id;
    private String description;
    protected TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        this.name = name;
        this.description = description;
        this.status = status;
        countTaskAllTime += 1;
        id = countTaskAllTime;
    }

    public Task(String name, String description) { //Констурктор для использования в эпике(без записи статуса)
        this.name = name;
        this.description = description;
        countTaskAllTime += 1;
        id = countTaskAllTime;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
