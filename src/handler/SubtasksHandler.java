package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint4.model.Subtask;
import com.yandex.sprint4.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class SubtasksHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
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
        } catch (NoSuchElementException | IllegalArgumentException | Error e) {
            sendError(exchange);
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            Gson gson = getJson();
            sendText(exchange, gson.toJson(manager.getAllSubtasks()));
        } else if (split.length == 3) {
            Subtask subtask = manager.getSubtask(Integer.parseInt(split[2]));
            if (subtask != null) {
                Gson gson = getJson();
                sendText(exchange, gson.toJson(subtask));
            } else {
                sendNotGound(exchange);
            }
        }
    }

    private void post(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = getJson();
            if (manager.add(gson.fromJson(body, Subtask.class)))
                sendText(exchange, "Подзадача успешно добавлена!");
            else
                sendHasInteractions(exchange);
        } else if (split.length == 3) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = getJson();
            Subtask subtask = gson.fromJson(body, Subtask.class);
            if (manager.update(subtask))
                sendText(exchange, "Подзадача успешно обновлена!");
            else
                sendHasInteractions(exchange);
        }
    }

    private void delete(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            manager.removeAllSubtasks();
            sendText(exchange, "Все подзадачи успешно удалены!");
        } else if (split.length == 3) {
            if (manager.getSubtask(Integer.parseInt(split[2])) != null) {
                manager.removeSubtask(Integer.parseInt(split[2]));
                sendText(exchange, "Подзадача с Id = " + split[2] + " успешно удалена!");
            } else {
                sendNotGound(exchange);
            }
        }
    }
}
