package com.yandex.sprint4.model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;

    public Epic(String name, String description, ArrayList<Integer> subtaskId) { //Конструктор для нескольких ID подзадач
        super(name, description);
        this.subtasksId = new ArrayList<>();
        subtasksId.addAll(subtaskId);

    }

    public Epic(String name, String description, int subtaskId) { //Конструктор для одного ID подзадачи
        super(name, description);
        this.subtasksId = new ArrayList<>();
        subtasksId.add(subtaskId);
    }

    public Epic(String name, String description) { //Конструктор без ID подзадач
        super(name, description);
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtasksId = epic.subtasksId;
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
    }

    public void setStatus(TaskStatus status) {
        super.status = status;
    }

    public void removeSubtask(Integer subtaskIdRemove) {
        if (subtasksId.contains(subtaskIdRemove))
            subtasksId.remove(subtaskIdRemove);
    }

}
