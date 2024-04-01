package com.yandex.sprint4.service;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getHistory());
    }

    public static HistoryManager getHistory() {
        return new InMemoryHistoryManager();
    }
}
