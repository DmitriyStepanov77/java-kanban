package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint4.model.Epic;
import com.yandex.sprint4.service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

public class EpicsHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public EpicsHandler(TaskManager manager) {
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
            sendText(exchange, gson.toJson(manager.getAllEpics()));
        } else if (split.length == 3) {
            Epic epic = manager.getEpic(Integer.parseInt(split[2]));
            if (epic != null) {
                Gson gson = getJson();
                sendText(exchange, gson.toJson(epic));
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
            manager.add(gson.fromJson(body, Epic.class));
            sendText(exchange, "Эпик успешно добавлен!");
        } else if (split.length == 3) {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = getJson();
            Epic epic = gson.fromJson(body, Epic.class);
            manager.update(epic);
            sendText(exchange, "Эпик успешно обновлен!");
        }
    }

    private void delete(HttpExchange exchange) throws IOException {
        String[] split = exchange.getRequestURI().getPath().split("/");
        if (split.length == 2) {
            manager.removeAllEpics();
            sendText(exchange, "Все эпики успешно удалены!");
        } else if (split.length == 3) {
            if (manager.getEpic(Integer.parseInt(split[2])) != null) {
                manager.removeEpic(Integer.parseInt(split[2]));
                sendText(exchange, "Эпик с Id = " + split[2] + " успешно удален!");
            } else {
                sendNotGound(exchange);
            }
        }
    }

}
