package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager fileTaskManager;
    Task task1;
    Task task2;

    @BeforeEach
    void loadFromFile() {
        fileTaskManager = FileBackedTaskManager.loadFromFile(new File("Save.csv"));

        task1 = new Task("Задача 1", "Описание 1", TaskStatus.NEW);
        task2 = new Task("Задача 2", "Описание 2", TaskStatus.NEW);

        task1.setId(1);
        task1.setId(2);
    }

    @Test
    void writeTaskTest() throws IOException {

        fileTaskManager.add(task1);
        fileTaskManager.add(task2);

        List<String> strs = Files.readAllLines(Path.of("Save.csv"));
        String[] split = strs.get(0).split(";");
        assertEquals(task1.getName(), split[2]);
    }

    @Test
    void readTaskTest() {
        assertEquals(fileTaskManager.getTask(2).getName(), task2.getName());
    }

    @Test
    void removeTaskTest() throws IOException {
        fileTaskManager.add(task1);
        fileTaskManager.add(task2);

        fileTaskManager.removeTask(1);

        List<String> strs = Files.readAllLines(Path.of("Save.csv"));
        String[] split = strs.get(0).split(";");
        assertEquals(task2.getName(), split[2]);
    }
}