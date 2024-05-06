package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

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

    @Test
    public void getHistoryTestAfterRemoveAll() {
        inMemoryHistoryManager.remove(task1.getId());
        inMemoryHistoryManager.remove(task2.getId());
        assertEquals(new ArrayList<>(), inMemoryHistoryManager.getHistory());
    }

    @Test
    public void getHistoryEpicTest() {
        Epic epic = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(5, 6, 7)));
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1 Эпика 1", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2 Эпика 1", TaskStatus.DONE, 3);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3 Эпика 1", TaskStatus.DONE, 3);

        epic.setId(3);
        subtask1.setId(5);
        subtask2.setId(6);
        subtask3.setId(7);

        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(subtask1);
        inMemoryHistoryManager.add(subtask2);
        inMemoryHistoryManager.add(subtask3);
        assertEquals("Эпик 1", inMemoryHistoryManager.getHistory().get(2).getName());

        inMemoryHistoryManager.remove(epic.getId());
        assertEquals("Подзадача 1", inMemoryHistoryManager.getHistory().get(2).getName());
    }
}