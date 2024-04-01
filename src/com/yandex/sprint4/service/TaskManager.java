package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasksList() //Получение всех задач
    ;

    List<Epic> getEpicList() //Получение всех эпиков
    ;

    List<Subtask> getSubtaskList() //Получение всех подзадач
    ;

    void add(Task task) //Добавление задачи
    ;

    void add(Epic epic) //Добавление эпика
    ;

    void add(Subtask subtask) //Добавление подзадачи
    ;

    void update(Task task) //Обновление задачи
    ;

    void update(Epic epic) //Обновление эпика
    ;

    void update(Subtask subtask) //Обновление подзадачи
    ;

    Task getTask(int key) //Получение задачи
    ;

    Epic getEpic(int key) //Получение эпика
    ;

    Subtask getSubtask(int key) //Получение подзадачи
    ;

    void removeAllTask() //Удаление всех задач
    ;

    void removeAllEpic() //Удаление всех эпиков
    ;

    void removeAllSubtask() //Удаление всех подзадач
    ;

    void removeTask(int key) //Удаление задачи
    ;

    void removeEpic(int key) //Удаление эпика
    ;

    void removeSubtask(int key) //Удаление подзадачи
    ;

    ArrayList<Subtask> getAllSubtaskToEpic(int epicId) //Получение подзадач эпика
    ;
    public List<Task> getHistory() //Получение истории
    ;

}


