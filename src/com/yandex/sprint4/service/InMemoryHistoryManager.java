package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final int MAX_COUNT = 10;

    private ArrayList<Task> historyList = new ArrayList<>();
    @Override
    public List<Task> getHistory() { //Показ истории
        return historyList;
    }

    @Override
    public void add(Task task) { //Добавление задачи в историю
        if (historyList.size() < MAX_COUNT)
            historyList.add(task);
        else {
            historyList.removeFirst();
            historyList.add(task);
        }
    }

}
