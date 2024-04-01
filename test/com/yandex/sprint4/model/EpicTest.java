package com.yandex.sprint4.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void testId() {
        Epic epic1 = new Epic("Задача 1", "Описание задачи 1", 1);
        epic1.setId(1);
        Epic epic2 = new Epic("Задача 1", "Описание задачи 1", 1);
        epic2.setId(1);
        epic1.equals(epic2);
    }

}