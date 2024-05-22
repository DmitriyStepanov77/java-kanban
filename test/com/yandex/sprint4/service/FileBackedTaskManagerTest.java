package com.yandex.sprint4.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest {
    FileBackedTaskManager fileTaskManager;

    @BeforeEach
    void loadFile() throws IOException {
        Files.writeString(Path.of("Save.csv"), "");
    }

    @Test
    void writeTaskTest() throws IOException {
        fileTaskManager = FileBackedTaskManager.loadFromFile(new File("Save.csv"));

        fileTaskManager.add(task1);
        fileTaskManager.add(task2);

        List<String> strs = Files.readAllLines(Path.of("Save.csv"));
        String[] split = strs.get(strs.size() - 2).split(";");
        assertEquals(task1.getName(), split[2]);
    }

    @Test
    void readTaskTest() throws IOException {
        Files.writeString(Path.of("Save.csv"), "TASK;2;Задача 2;Описание 2;NEW;PT21H;2024-01-12T13:24");
        FileBackedTaskManager fileTaskManagerRead = FileBackedTaskManager.loadFromFile(new File("Save.csv"));
        assertEquals(fileTaskManagerRead.getTask(2).getName(), task2.getName());
        assertEquals(fileTaskManagerRead.getTask(2).getStartTime(), task2.getStartTime());
    }

    @Test
    void removeTaskTest() throws IOException {
        fileTaskManager = FileBackedTaskManager.loadFromFile(new File("Save.csv"));

        fileTaskManager.add(task1);
        fileTaskManager.add(task2);

        fileTaskManager.removeTask(2);

        List<String> strs = Files.readAllLines(Path.of("Save.csv"));
        String[] split = strs.get(strs.size() - 1).split(";");
        assertEquals(task1.getName(), split[2]);
    }

    @Test
    void writeIncorrectData1() throws IOException {

        Files.writeString(Path.of("Save.csv"), "TASK;1;Задача 1;Описание 1;NEW;null;null" + "\n", StandardOpenOption.APPEND);
        Files.writeString(Path.of("Save.csv"), "Hello World !!", StandardOpenOption.APPEND);

        FileBackedTaskManager fileTaskManagerRead = FileBackedTaskManager.loadFromFile(new File("Save.csv"));

        assertEquals(fileTaskManagerRead.getTask(1).getName(), "Задача 1");
    }

    @Test
    void writeIncorrectData2() throws IOException {

        Files.writeString(Path.of("Save.csv"), "TASK;1;Задача 1;Описание 1;NEW;null;null" + "\n", StandardOpenOption.APPEND);
        Files.writeString(Path.of("Save.csv"), "Hello World !!" + "\n", StandardOpenOption.APPEND);
        Files.writeString(Path.of("Save.csv"), "TASK;2;Задача 3;Описание 3;NEW;null;null" + "\n", StandardOpenOption.APPEND);
        Files.writeString(Path.of("Save.csv"), "EPIC;3;Ll;dls;fs;NEW;[4, 5]" + "\n", StandardOpenOption.APPEND);
        Files.writeString(Path.of("Save.csv"), "EPIC;3;Эпик 1;Описание 4;NEW;[4, 5];null;null", StandardOpenOption.APPEND);

        FileBackedTaskManager fileTaskManagerRead = FileBackedTaskManager.loadFromFile(new File("Save.csv"));

        assertEquals(fileTaskManagerRead.getTask(2).getName(), "Задача 3");
        assertEquals(fileTaskManagerRead.getEpic(3).getName(), "Эпик 1");
    }
}