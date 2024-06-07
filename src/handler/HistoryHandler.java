package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint4.model.Task;
import com.yandex.sprint4.service.TaskManager;

import java.io.IOException;
import java.util.NoSuchElementException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("GET".equals(exchange.getRequestMethod())) {
                get(exchange);
            } else {
                sendError(exchange);
            }
        }catch (NoSuchElementException | IllegalArgumentException | Error e) {
            sendError(exchange);
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        Gson gson = getJson();
        sendText(exchange, gson.toJson(manager.getHistory()));
    }
}
