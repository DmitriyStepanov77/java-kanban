package com.yandex.sprint4.model;

public class Subtask extends Task {
    private int epicId = 0;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) { //Конструктор для одного эпик ID
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status) { //Конструктор без эпик ID
        super(name, description, status);
    }

    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.epicId;

    }
}
