package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    @Test
    public void updateTaskTest() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        inMemoryTaskManager.add(task1);
        Task taskTemp = inMemoryTaskManager.getTask(1);
        taskTemp.setName("Задача 2");
        inMemoryTaskManager.update(taskTemp);

        assertEquals("Задача 2", inMemoryTaskManager.getTask(1).getName());
    }

    @Test
    public void updateSubtaskTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(2, 3)));

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 1);

        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);

        Subtask subtaskTemp = inMemoryTaskManager.getSubtask(2);
        subtaskTemp.setStatus(TaskStatus.DONE);
        inMemoryTaskManager.update(subtaskTemp);

        assertEquals(TaskStatus.DONE, inMemoryTaskManager.getEpic(1).getStatus());

    }

    @Test
    public void addAndRemoveEpicTest() {
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(2, 3)));

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 1);

        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);

        inMemoryTaskManager.getEpic(1);
        inMemoryTaskManager.getSubtask(2);

        assertEquals("Эпик 1", inMemoryTaskManager.getHistory().get(0).getName());

        inMemoryTaskManager.removeEpic(1);

        assertEquals(new ArrayList<>(), inMemoryTaskManager.getHistory());
    }


}