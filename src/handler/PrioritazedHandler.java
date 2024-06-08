package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.sprint4.service.TaskManager;

import java.io.IOException;
import java.util.NoSuchElementException;

public class PrioritazedHandler extends BaseHttpHandler implements HttpHandler {

    TaskManager manager;

    public PrioritazedHandler(TaskManager manager) {
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
        } catch (Exception e) {
            sendError(exchange);
        }
    }

    private void get(HttpExchange exchange) throws IOException {
        Gson gson = getJson();
        sendText(exchange, gson.toJson(manager.getPrioritizedTasks()));
    }
}
