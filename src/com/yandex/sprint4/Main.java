package com.yandex.sprint4;


import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import com.yandex.sprint4.model.*;
import com.yandex.sprint4.service.FileBackedTaskManager;
import com.yandex.sprint4.service.InMemoryTaskManager;
import com.yandex.sprint4.service.Managers;

public class Main {

    public static void main(String[] args) throws IOException {
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        System.out.println("Спринт 6. Пользовательский сценарий");

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW,
                Duration.ofHours(5), LocalDateTime.of(2023, 10, 11, 14, 23));
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW,
                Duration.ofHours(6), LocalDateTime.of(2023, 9, 11, 14, 23));

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(5, 6, 7)));
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2");

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1 Эпика 1", TaskStatus.NEW, 3,
                Duration.ofHours(2), LocalDateTime.of(2023, 9, 12, 14, 23));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2 Эпика 1", TaskStatus.DONE, 3,
                Duration.ofHours(2), LocalDateTime.of(2023, 9, 13, 14, 23));
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3 Эпика 1", TaskStatus.DONE, 3,
                Duration.ofHours(2), LocalDateTime.of(2023, 9, 14, 14, 23));

        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);

        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(epic2);

        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);
        inMemoryTaskManager.add(subtask3);

        System.out.println("Вывод истории. Вариант 1:");
        System.out.println(inMemoryTaskManager.getHistory());
        Task task = inMemoryTaskManager.getTask(1);
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        task = inMemoryTaskManager.getEpic(4);
        task = inMemoryTaskManager.getSubtask(5);
        task = inMemoryTaskManager.getSubtask(6);
        task = inMemoryTaskManager.getSubtask(7);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Вывод истории. Вариант 2:");
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Вывод истории. Вариант 2.1:");
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeAllEpics();
        System.out.println(inMemoryTaskManager.getAllTasksList());
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println("Вывод истории. Вариант 2.2:");
        inMemoryTaskManager.removeAllTasks();
        System.out.println(inMemoryTaskManager.getAllTasksList());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Вывод истории. Вариант 3:");
        inMemoryTaskManager.removeTask(1);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Вывод истории. Вариант 4:");
        inMemoryTaskManager.removeEpic(3);
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Спринт 7. Пользовательский сценарий");
        FileBackedTaskManager fileTaskManager = FileBackedTaskManager.loadFromFile(new File("Save.csv"));
        fileTaskManager.add(task1);
        fileTaskManager.add(task2);
        fileTaskManager.add(epic1);
        fileTaskManager.add(epic2);
        System.out.println(fileTaskManager.getAllTasksList());
        System.out.println(fileTaskManager.getAllEpics());
        fileTaskManager.removeAllEpics();

    }
}
