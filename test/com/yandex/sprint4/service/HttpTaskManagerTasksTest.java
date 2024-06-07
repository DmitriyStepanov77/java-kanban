package com.yandex.sprint4.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.sprint4.model.Adapters.DurationTypeAdapter;
import com.yandex.sprint4.model.Adapters.LocalDateTimeTypeAdapter;
import com.yandex.sprint4.model.Adapters.LocalTimeTypeAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Task task1;
    Task task2;
    Task task3;
    TaskManager manager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = getJson();

    @BeforeEach
    public void setUp() throws IOException {
        epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(2, 3)));

        epic2 = new Epic("Эпик 2", "Описание эпика 2", 5);

        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.NEW, 1,
                Duration.ofHours(20), LocalDateTime.of(2025, 10, 12, 13, 24));
        subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 1,
                Duration.ofHours(20), LocalDateTime.of(2025, 10, 12, 13, 24));
        subtask3 = new Subtask("Подзадача 3", "Описание подзадачи 3", TaskStatus.DONE, 4,
                Duration.ofHours(20), LocalDateTime.of(2025, 10, 12, 13, 24));

        task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW,
                Duration.ofHours(20), LocalDateTime.of(2024, 10, 12, 13, 24));
        task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW,
                Duration.ofHours(21), LocalDateTime.of(2024, 1, 12, 13, 24));
        task3 = new Task("Задача 3", "Описание задачи 3", TaskStatus.NEW,
                Duration.ofHours(22), LocalDateTime.of(2024, 8, 12, 13, 24));

        manager.removeAllTasks();
        manager.removeAllEpics();
        manager.removeAllSubtasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Задача 1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException {
        manager.add(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(task, manager.getTask(1), "Некорректная задача");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        manager.add(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = manager.getAllTasksList();

        assertEquals(0, tasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        manager.add(task1);
        manager.add(task2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(2, tasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        String taskJson = gson.toJson(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> tasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Подзадача 1", tasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        manager.add(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(subtask, manager.getSubtask(1), "Некорректная задача");
    }

    @Test
    public void testDeleteSutask() throws IOException, InterruptedException {
        manager.add(subtask1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = manager.getAllSubtasks();

        assertEquals(0, subtasks.size(), "Некорректное количество подзадач");
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        manager.add(subtask1);
        manager.add(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Subtask> subtasks = gson.fromJson(response.body(), new SubtaskTypeToken().getType());

        assertEquals(2, subtasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        String taskJson = gson.toJson(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "Эпики не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Эпик 1", tasksFromManager.get(0).getName(), "Некорректное имя эпика");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        manager.add(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic, manager.getEpic(1), "Некорректный эпик");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        manager.add(epic1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> epics = manager.getAllEpics();

        assertEquals(0, epics.size(), "Некорректное количество эпиков");
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        manager.add(epic1);
        manager.add(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Epic> epics = gson.fromJson(response.body(), new EpicTypeToken().getType());

        assertEquals(2, epics.size(), "Некорректное количество эпиков");
    }

    @Test
    public void testGetPrioretized() throws IOException, InterruptedException {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioretized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(task2, tasks.get(0), "Неправильный приоритет");
        assertEquals(task1, tasks.get(2), "Неправильный приоритет");
    }

    @Test
    public void testGetHistiry() throws IOException, InterruptedException {
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);

        Task task = manager.getTask(2);
        task = manager.getTask(1);
        task = manager.getTask(3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TaskTypeToken().getType());

        assertEquals(task2, tasks.get(0), "Неправильный приоритет");
        assertEquals(task1, tasks.get(1), "Неправильный приоритет");
        assertEquals(task3, tasks.get(2), "Неправильный приоритет");
    }

    protected Gson getJson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter());
        return gsonBuilder.create();
    }

    class TaskTypeToken extends TypeToken<List<Task>> {
    }

    class SubtaskTypeToken extends TypeToken<List<Subtask>> {
    }

    class EpicTypeToken extends TypeToken<List<Epic>> {
    }
}