package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private ArrayList<Task> historyList = new ArrayList<>();
    @Override
    public List<Task> getHistory() { //Показ истории
        return historyList;
    }

    @Override
    public void add(Task task) { //Добавление задачи в историю
        if (historyList.size() != 10)
            historyList.add(new Task(task));
        else {
            historyList.remove(0);
            historyList.add(new Task(task));
        }
    }

}
