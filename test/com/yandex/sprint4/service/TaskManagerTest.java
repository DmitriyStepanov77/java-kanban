package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    Epic epic1;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    public void beforeEachAll() throws IOException {
        epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(2, 3)));
        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1);
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 1);
        subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.DONE, 1);

        task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW,
                Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 13, 24));
        task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW,
                Duration.ofHours(21), LocalDateTime.of(2024, 1, 12, 13, 24));
        task3 = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW,
                Duration.ofHours(22), LocalDateTime.of(2024, 8, 12, 13, 24));

        Files.writeString(Path.of("Save.csv"), "");
    }

    @Test
    public void updateTaskTest() throws IOException {
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        taskManager.add(task1);
        Task taskTemp = taskManager.getTask(1);
        taskTemp.setName("Задача 2");
        taskManager.update(taskTemp);

        assertEquals("Задача 2", taskManager.getTask(1).getName());

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            String[] split = strs.getLast().split(";");
            assertEquals("Задача 2", split[2]);
        }
    }

    @Test
    public void updateSubtaskTest() throws IOException {
        taskManager.add(epic1);
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        Subtask subtaskTemp = taskManager.getSubtask(2);
        subtaskTemp.setStatus(TaskStatus.DONE);
        taskManager.update(subtaskTemp);
        assertEquals(TaskStatus.DONE, taskManager.getEpic(1).getStatus());

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            String[] split = strs.getLast().split(";");
            assertEquals(TaskStatus.DONE, TaskStatus.valueOf(split[4]));
        }
    }

    @Test
    public void addAndRemoveEpicTest() throws IOException {
        taskManager.add(epic1);
        taskManager.add(subtask1);
        taskManager.add(subtask2);

        taskManager.getEpic(1);
        taskManager.getSubtask(2);

        assertEquals("Эпик 1", taskManager.getHistory().getFirst().getName());

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            String[] split = strs.getLast().split(";");
            assertEquals("Эпик 1", split[2]);
        }

        taskManager.removeEpic(1);

        assertEquals(new ArrayList<>(), taskManager.getHistory());

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            assertEquals(new ArrayList<>(), strs);
        }
    }

    @Test
    public void getPrioritizedTasksTest() {
        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);

        assertEquals("Задача 2", taskManager.getPrioritizedTasks().get(0).getName());
        assertEquals("Задача 1", taskManager.getPrioritizedTasks().get(2).getName());

        taskManager.removeTask(2);

        assertEquals("Задача 3", taskManager.getPrioritizedTasks().get(0).getName());
    }

    @Test
    public void chechIntersectionIntervalTest() throws IOException {
        Task task4 = new Task("Задача 4", "Описание задачи 4", TaskStatus.NEW,
                Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 16, 24));

        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(task3);

        taskManager.add(task4);

        assertNull(taskManager.getTask(4)); //Проверка если задача пересекается

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            String[] split = strs.getLast().split(";");
            assertEquals("Задача 3", split[2]);
        }

        task4.setStartTime(LocalDateTime.of(2024, 10, 15, 15, 20));

        taskManager.add(task4);

        assertEquals(task4, taskManager.getTask(4));//Проверка если задача не пересекается

        if (taskManager.getClass().getSimpleName().equals("FileBackedTaskManager")) {
            List<String> strs = Files.readAllLines(Path.of("Save.csv"));
            String[] split = strs.getLast().split(";");
            assertEquals("Задача 4", split[2]);
        }
    }

}
