package com.yandex.sprint4;


import java.util.ArrayList;
import java.util.Arrays;

import com.yandex.sprint4.model.*;
import com.yandex.sprint4.service.InMemoryTaskManager;
import com.yandex.sprint4.service.Managers;

public class Main {

    public static void main(String[] args) {
        //Тесты
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();

        System.out.println("Поехали!");

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(5,6)));
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 2", 32);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 3);
        Subtask subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.DONE, 4);

        Epic epic3 = new Epic("Эпик 3", "Описание эпика 3", 9);
        Subtask subtask4 = new Subtask("Подзадача 4", "Описание подзадачи 4", TaskStatus.IN_PROGRESS, 8);

        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);

        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(epic2);

        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);
        inMemoryTaskManager.add(subtask3);

        inMemoryTaskManager.add(epic3);
        inMemoryTaskManager.add(subtask4);

        Task task = inMemoryTaskManager.getTask(1);
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        task = inMemoryTaskManager.getTask(1);
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        task = inMemoryTaskManager.getTask(1);
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        task = inMemoryTaskManager.getTask(1);
        task = inMemoryTaskManager.getTask(2);
        task = inMemoryTaskManager.getEpic(3);
        System.out.println(inMemoryTaskManager.getHistory());
        //System.out.println("Список подзадач:");
        //System.out.println(manager.getSubtaskList());

        //System.out.println("Список эпиков:");
        //System.out.println(manager.getEpicList());

        //System.out.println("Свойства эпика 1:");
        //System.out.println(manager.getEpic(3));

        //System.out.println("Список подзадач эпика 3:");
        //System.out.println(manager.getAllSubtaskToEpic(3));

        //System.out.println("Удаление подзадачи эпика 3:");
        //manager.removeSubtask(5);
        //System.out.println(manager.getEpic(3));
        //System.out.println(manager.getAllSubtaskToEpic(3));

        //System.out.println("Удаление эпика 3");
        //manager.removeEpic(3);
        //System.out.println("Вывод подзадач:");
        //System.out.println(manager.getSubtaskList());
        //System.out.println(manager.getEpicList());
        //System.out.println("Удаление подзадач:");
        //manager.removeAllSubtask();
        //System.out.println(manager.getSubtaskList());
        //System.out.println("Удаление эпиков:");
        //manager.removeAllEpic();
        //System.out.println(manager.getEpicList());

    }
}
