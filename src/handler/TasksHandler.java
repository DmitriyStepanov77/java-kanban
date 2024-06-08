package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                get(exchange);
            } else if ("POST".equals(exchange.getRequestMethod())) {
                post(exchange);
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                delete(exchange);
            } else {
                sendError(exchange);
            }
        } catch (Exception e) {
            sendError(exchange);
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            Gson gson = getJson();
            sendText(exchange, gson.toJson(manager.getAllTasksList()));
        } else if (split.length == 3) {
            Task task = manager.getTask(Integer.parseInt(split[2]));
            if (task != null) {
                Gson gson = getJson();
                sendText(exchange, gson.toJson(task));
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void post(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = getJson();
            if (manager.add(gson.fromJson(body, Task.class)))
                sendText(exchange, "Задача успешно добавлена!");
            else
                sendHasInteractions(exchange);
        } else if (split.length == 3) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = getJson();
            Task task = gson.fromJson(body, Task.class);
            if (manager.update(task))
                sendText(exchange, "Задача успешно обновлена!");
            else
                sendHasInteractions(exchange);
        }
    }

    private void delete(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            manager.removeAllTasks();
            sendText(exchange, "Все задачи успешно удалены!");
        } else if (split.length == 3) {
            if (manager.getTask(Integer.parseInt(split[2])) != null) {
                manager.removeTask(Integer.parseInt(split[2]));
                sendText(exchange, "Задача с Id = " + split[2] + " успешно удалена!");
            } else {
                sendNotFound(exchange);
            }
        }
    }
}
