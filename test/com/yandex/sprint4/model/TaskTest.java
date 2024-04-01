package com.yandex.sprint4.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void testId() {
        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        task1.setId(1);
        Task task2 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        task2.setId(1);
        task1.equals(task2);
    }

}