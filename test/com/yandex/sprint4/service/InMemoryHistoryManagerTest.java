package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    Task task1;
    Task task2;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();

        task1 = new Task("1", "2", TaskStatus.NEW);
        task2 = new Task("2", "3", TaskStatus.NEW);

        task1.setId(1);
        task1.setId(2);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task1);
    }

    @Test
    public void getHistoryTest() {
        assertEquals("2", inMemoryHistoryManager.getHistory().get(0).getName());
        assertEquals("1", inMemoryHistoryManager.getHistory().get(1).getName());
    }

    @Test
    public void getHistoryTestAfterRemove() {
        inMemoryHistoryManager.remove(task1.getId());
        assertEquals("2", inMemoryHistoryManager.getHistory().get(0).getName());
    }

    public void getHistoryTestAfterRemoveAll() {
        inMemoryHistoryManager.remove(task1.getId());
        inMemoryHistoryManager.remove(task2.getId());
        assertEquals("", inMemoryHistoryManager.getHistory().get(0).getName());
    }

}