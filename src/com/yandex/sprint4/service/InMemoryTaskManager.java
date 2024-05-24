package com.yandex.sprint4.service;

import com.yandex.sprint4.model.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;
    private Set<Task> sortedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getAllTasksList() {
        return new ArrayList<>(tasks.values());
    } //Получение всех задач

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    } //Получение всех эпиков

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    } //Получение всех подзадач

    @Override
    public void add(Task task) {
        if (checkTimeIntersection(task)) {
            task.setId(id);
            tasks.put(task.getId(), task);
            addSortedTasks(tasks.get(id));
            id++;
        } else {
            System.out.println("Время выполнение задачи пересекается с другими задачами!");
        }
    } //Добавление задачи

    @Override
    public void add(Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(), epic);
        updateEpic(epic.getId());
        id++;
    } //Добавление эпика

    @Override
    public void add(Subtask subtask) {
        if (checkTimeIntersection(subtask)) {
            subtask.setId(id);
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getEpicId() != 0) {
                updateEpic(subtask.getEpicId());
            }
            addSortedTasks(subtasks.get(id));
            id++;
        } else {
            System.out.println("Время выполнение задачи пересекается с другими задачами!");
        }
    } //Добавление подзадачи

    @Override
    public void update(Task task) {
        sortedTasks.remove(subtasks.get(task.getId()));
        tasks.replace(task.getId(), task);
        addSortedTasks(task);
    } //Обновление задачи

    @Override
    public void update(Epic epic) {
        epics.replace(epic.getId(), epic);
        updateEpic(epic.getId());
    } //Обновление эпика

    @Override
    public void update(Subtask subtask) {
        sortedTasks.remove(subtasks.get(subtask.getId()));
        subtasks.replace(subtask.getId(), subtask);
        addSortedTasks(subtask);
        if (subtask.getEpicId() != 0) {
            updateEpic(subtask.getEpicId());
        }
    } //Обновление подзадачи

    @Override
    public Task getTask(int key) {
        if (tasks.containsKey(key)) {
            historyManager.add(tasks.get(key));
            return tasks.get(key);
        } else
            return null;
    } //Получение задачи

    @Override
    public Epic getEpic(int key) {
        if (epics.containsKey(key)) {
            historyManager.add(epics.get(key));
            return epics.get(key);
        } else
            return null;
    } //Получение эпика

    @Override
    public Subtask getSubtask(int key) {
        if (subtasks.containsKey(key)) {
            historyManager.add(subtasks.get(key));
            return subtasks.get(key);
        } else
            return null;
    } //Получение подзадачи

    @Override
    public void removeAllTasks() {
        tasks.keySet().stream().toList().forEach(this::removeTask);
    } //Удаление всех задач

    @Override
    public void removeAllEpics() {
        epics.keySet().stream().toList().forEach(this::removeEpic);
    } //Удаление всех эпиков

    @Override
    public void removeAllSubtasks() {
        subtasks.keySet().stream().toList().forEach(this::removeSubtask);
    } //Удаление всех подзадач

    @Override
    public void removeTask(int key) {
        if (tasks.containsKey(key)) {
            if (historyManager.getHistory().contains(tasks.get(key))) {
                historyManager.remove(key);
            }
            sortedTasks.remove(tasks.get(key));
            tasks.remove(key);
        }
    } //Удаление задачи

    @Override
    public void removeEpic(int key) {
        if (epics.containsKey(key)) {
            Epic epic = epics.get(key); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) { //Проверяем наличие подзадач для эпика
                subtaskEpicList.stream()
                        .filter(Objects::nonNull)
                        .forEach(o -> {
                            subtasks.remove(o);
                            historyManager.remove(o);
                        });
            }
            epics.remove(key);
            historyManager.remove(key);
        }
    } //Удаление эпика

    @Override
    public void removeSubtask(int key) {
        if (subtasks.containsKey(key)) {
            Integer epicId = subtasks.get(key).getEpicId();
            if (epics.containsKey(epicId)) {
                epics.get(epicId).removeSubtask(key);
                updateEpic(epicId);
            }
            if (historyManager.getHistory().contains(subtasks.get(key))) {
                historyManager.remove(key);
            }
            sortedTasks.remove(subtasks.get(key));
            subtasks.remove(key);
        }
    } //Удаление подзадачи

    @Override
    public ArrayList<Subtask> getAllSubtasksToEpic(int epicId) {
        if (epics.containsKey(epicId)) { //Проверяем, есть ли в списке эпиков эпик с нужным ID
            Epic epic = epics.get(epicId); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) {
                return new ArrayList<>(subtaskEpicList.stream()
                        .map(o -> subtasks.get(o))
                        .collect(Collectors.toList()));
            } else {
                return null;
            }
        } else
            return null;
    } //Получение всех подзадач эпика

    private void updateEpic(int id) {
        if (epics.containsKey(id)) { //Проверяем наличие эпика с нужным ID
            Epic epic = epics.get(id); //Получаем эпик по ID
            ArrayList<Subtask> subtaskToEpic = getAllSubtasksToEpic(id); //Получаем все подзадачи эпика
            if (subtaskToEpic != null) { //Проверяем наличие подзадач
                updateEpicStatus(epic, subtaskToEpic);
                updateEpicDuration(epic, subtaskToEpic);
                updateEpicStartTime(epic, subtaskToEpic);
                updateEpicEndTime(epic, subtaskToEpic);
            } else {
                epic.setStatus(TaskStatus.NEW); //Если подзадач нет, то статус - NEW
                epic.setStartTime(null); //Дата начала - NULL
                epic.setEndTime(null); //Дата завершения - NULL
                epic.setDuration(Duration.ZERO); //Продолжительность - 0
            }
        }
    } //Обновление статуса эпика

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedTasks);
    }

    private void updateEpicStatus(Epic epic, ArrayList<Subtask> subtaskToEpic) {
        boolean isNew = false, isInProgress = false, isDone = false;
        for (Subtask o : subtaskToEpic) {
            if (o != null) { //Проверяем, что подзадача существует
                switch (o.getStatus()) { //Получаем признаки присутствия каждого из статуса в подзадачах
                    case NEW -> isNew = true;
                    case IN_PROGRESS -> isInProgress = true;
                    case DONE -> isDone = true;
                }
            } else
                isNew = true;
        }
        if (isDone && !(isNew || isInProgress)) {
            epic.setStatus(TaskStatus.DONE);
        } else if (isNew && !(isDone || isInProgress)) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private void updateEpicDuration(Epic epic, ArrayList<Subtask> subtaskToEpic) {
        epic.setDuration(subtaskToEpic.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus)); //Получаем суммарную продолжительность всех подзадач
    }

    private void updateEpicStartTime(Epic epic, ArrayList<Subtask> subtaskToEpic) {
        epic.setStartTime(subtaskToEpic.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.comparing(startTime -> startTime))
                .orElse(null)); //Получаем время начала самой ранней подзадачи
    }

    private void updateEpicEndTime(Epic epic, ArrayList<Subtask> subtaskToEpic) {
        epic.setStartTime(subtaskToEpic.stream()
                .filter(Objects::nonNull)
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.comparing(endTime -> endTime))
                .orElse(null)); //Получаем время окончания самой поздней задачи
    }

    private void addSortedTasks(Task task) {
        if (task.getStartTime() != null) {
            sortedTasks.add(task);
        }
    }

    private boolean checkTimeIntersection(Task task) {
        List<Task> sortedList = getPrioritizedTasks();
        long count = sortedList.stream()
                .filter(o -> (o.getStartTime().isBefore(task.getStartTime())
                        && o.getEndTime().isAfter(task.getStartTime())) ||
                        (o.getStartTime().isBefore(task.getEndTime())
                                && o.getEndTime().isAfter(task.getEndTime()))).count();
        return count == 0;
    }


}
