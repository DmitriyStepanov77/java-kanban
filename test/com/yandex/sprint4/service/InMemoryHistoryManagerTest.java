package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    public void getHistoryTest() {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task1 = new Task("1", "2", TaskStatus.NEW);

        inMemoryHistoryManager.add(task1);

        task1.setName("2");

        assertEquals("1", inMemoryHistoryManager.getHistory().get(0).getName());
    }

}