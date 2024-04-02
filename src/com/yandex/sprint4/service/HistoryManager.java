package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory(); //Получение истории

    void add(Task task); //Добавление задачи в историю
}
