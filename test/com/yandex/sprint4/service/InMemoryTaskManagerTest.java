package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest {
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
        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);

        inMemoryTaskManager.getEpic(1);
        inMemoryTaskManager.getSubtask(2);

        assertEquals("Эпик 1", inMemoryTaskManager.getHistory().get(0).getName());

        inMemoryTaskManager.removeEpic(1);

        assertEquals(new ArrayList<>(), inMemoryTaskManager.getHistory());
    }

    @Test
    public void addAndRemoveTasksWithTimeTest() {
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(task3);

        assertEquals("Задача 2", inMemoryTaskManager.getPrioritizedTasks().get(0).getName());
        assertEquals("Задача 1", inMemoryTaskManager.getPrioritizedTasks().get(2).getName());

        inMemoryTaskManager.removeTask(2);

        assertEquals("Задача 3", inMemoryTaskManager.getPrioritizedTasks().get(0).getName());
    }

    @Test
    public void chechIntersectionIntervalTest() {
        Task task4 = new Task("Задача 4", "Описание задачи 4", TaskStatus.NEW,
                Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 16, 24));

        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(task3);

        inMemoryTaskManager.add(task4);

        assertEquals(null, inMemoryTaskManager.getTask(4));

        task4.setStartTime(LocalDateTime.of(2024, 10, 15, 15, 20));

        inMemoryTaskManager.add(task4);

        assertEquals(task4, inMemoryTaskManager.getTask(4));
    }


}