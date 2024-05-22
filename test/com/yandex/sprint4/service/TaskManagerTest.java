package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

abstract class TaskManagerTest {
    Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(2, 3)));
    Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1);
    Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 1);
    Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.DONE, 1);

    Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW,
            Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 13, 24));
    Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW,
            Duration.ofHours(21), LocalDateTime.of(2024, 01, 12, 13, 24));
    Task task3 = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW,
            Duration.ofHours(22), LocalDateTime.of(2024, 8, 12, 13, 24));
}
