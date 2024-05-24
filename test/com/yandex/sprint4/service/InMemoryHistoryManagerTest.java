package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;

    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    public void beforeEach() {
        inMemoryHistoryManager = new InMemoryHistoryManager();

        task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW,
                Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 13, 24));
        task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW,
                Duration.ofHours(21), LocalDateTime.of(2024, 1, 12, 13, 24));
        task3 = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW,
                Duration.ofHours(22), LocalDateTime.of(2024, 8, 12, 13, 24));

        task1.setId(1);
        task2.setId(2);
        task3.setId(3);

        inMemoryHistoryManager.add(task1);
        inMemoryHistoryManager.add(task2);
        inMemoryHistoryManager.add(task3);
    }

    @Test
    public void getHistoryTest() {
        assertEquals("Задача 1", inMemoryHistoryManager.getHistory().get(0).getName());
        assertEquals("Задача 2", inMemoryHistoryManager.getHistory().get(1).getName());
    }

    @Test
    public void getHistoryAfterRemoveFirstTest() {
        inMemoryHistoryManager.remove(task1.getId());
        assertEquals("Задача 2", inMemoryHistoryManager.getHistory().getFirst().getName());
    }

    @Test
    public void getHistoryAfterRemoveMiddleTest() {
        inMemoryHistoryManager.remove(task2.getId());
        assertEquals("Задача 3", inMemoryHistoryManager.getHistory().get(1).getName());
    }

    @Test
    public void getHistoryAfterRemoveLastTest() {
        inMemoryHistoryManager.remove(task3.getId());
        assertEquals("Задача 2", inMemoryHistoryManager.getHistory().getLast().getName());
    }

    @Test
    public void getHistoryTestAfterRemoveAll() {
        inMemoryHistoryManager.remove(task1.getId());
        inMemoryHistoryManager.remove(task2.getId());
        inMemoryHistoryManager.remove(task3.getId());
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