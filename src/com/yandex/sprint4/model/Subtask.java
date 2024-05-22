package com.yandex.sprint4.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private int epicId = 0;

    public int getEpicId() {
        return epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId) { //Конструктор для одного эпик ID
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, int epicId,
                   Duration duration, LocalDateTime startTime) { //Конструктор со временем
        super(name, description, status, duration, startTime);
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
