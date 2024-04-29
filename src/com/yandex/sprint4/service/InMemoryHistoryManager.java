package com.yandex.sprint4.service;

import com.yandex.sprint4.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;
    private Map<Integer, Node> history = new HashMap<>();

    @Override
    public List<Task> getHistory() { //Вывод истории
        List<Task> historyList = new ArrayList<>();
        Node currentNode = first;
        while (currentNode != null) {
            historyList.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return historyList;
    }

    @Override
    public void add(Task task) { //Добавление задачи в историю
        if (!history.containsKey(task.getId()))
            linkLast(task);
        else {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
    }

    private class Node {
        Task data;
        Node next;
        Node prev;

        Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    private void linkLast(Task data) {
        final Node l = last;
        final Node newNode = new Node(l, data, null);
        last = newNode;
        if (l == null)
            first = newNode;
        else
            l.next = newNode;
        history.put(data.getId(), newNode);
    }

    private void removeNode(Node node) {
        if (node != null) {
            Node prev = node.prev;
            Node next = node.next;
            if (prev == null) {
                next.prev = null;
                first = next;
            } else if (next == null) {
                prev.next = null;
                last = prev;
            } else {
                prev.next = next;
                next.prev = prev;
            }

            history.remove(node.data.getId());
        }
    }

}


