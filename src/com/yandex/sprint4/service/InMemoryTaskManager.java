package com.yandex.sprint4.service;

import com.yandex.sprint4.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private HistoryManager historyManager;

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
        task.setId(id);
        tasks.put(task.getId(),task);
        id++;
    } //Добавление задачи

    @Override
    public void add(Epic epic) {
        epic.setId(id);
        epics.put(epic.getId(),epic);
        updateEpicStatus(epic.getId());
        id++;
    } //Добавление эпика

    @Override
    public void add(Subtask subtask) {
        subtask.setId(id);
        subtasks.put(subtask.getId(),subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
        id++;
    } //Добавление подзадачи

    @Override
    public void update(Task task) {
        tasks.replace(task.getId(), task);
    } //Обновление задачи

    @Override
    public void update(Epic epic) {
        epics.replace(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    } //Обновление эпика

    @Override
    public void update(Subtask subtask) {
        subtasks.replace(subtask.getId(), subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
    } //Обновление подзадачи

    @Override
    public Task getTask(int key) {
        if(tasks.containsKey(key)) {
            historyManager.add(tasks.get(key));
            return tasks.get(key);
        }
        else
            return null;
    } //Получение задачи

    @Override
    public Epic getEpic(int key) {
        if(epics.containsKey(key)) {
            historyManager.add(epics.get(key));
            return epics.get(key);
        }
        else
            return null;
    } //Получение эпика

    @Override
    public Subtask getSubtask(int key) {
        if(subtasks.containsKey(key)) {
            historyManager.add(subtasks.get(key));
            return subtasks.get(key);
        }
        else
            return null;
    } //Получение подзадачи

    @Override
    public void removeAllTasks() {
        tasks.clear();
    } //Удаление всех задач

    @Override
    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    } //Удаление всех эпиков

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Integer i : epics.keySet()) {
            updateEpicStatus(i);
        }
    } //Удаление всех подзадач

    @Override
    public void removeTask(int key) {
        if(tasks.containsKey(key)) {
            tasks.remove(key);
            historyManager.remove(key);
        }
    } //Удаление задачи

    @Override
    public void removeEpic(int key) {
        if(epics.containsKey(key)) {
            Epic epic = epics.get(key); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) { //Проверяем наличие подзадач для эпика
                for (Integer o : subtaskEpicList) {
                   if (o != null && subtasks.containsKey(o)) {
                       subtasks.remove(o);
                       historyManager.remove(o);
                   }
                }
            }
            epics.remove(key);
            historyManager.remove(key);
        }
    } //Удаление эпика

    @Override
    public void removeSubtask(int key) {
        if(subtasks.containsKey(key)) {
            Integer epicId = subtasks.get(key).getEpicId();
            if(epics.containsKey(epicId)) {
                Epic epic = epics.get(epicId);
                epic.removeSubtask(key);
                updateEpicStatus(epicId);
            }
            subtasks.remove(key);
            historyManager.remove(key);
        }
    } //Удаление подзадачи
    @Override
    public ArrayList<Subtask> getAllSubtasksToEpic(int epicId) {
        if(epics.containsKey(epicId)) { //Проверяем, есть ли в списке эпиков эпик с нужным ID
            Epic epic = epics.get(epicId); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) { //Проверяем наличие подзадач для эпика
                ArrayList<Subtask> subtasksToEpic = new ArrayList<>(); //Создаем новый список, в который запишем подзадачи
                for (Integer o : subtaskEpicList)
                    subtasksToEpic.add(subtasks.get(o)); //Дабавляем из хэш-карты подзадач задачи с нужными ID
                return subtasksToEpic;
            } else
                return null;
        } else
            return null;
    } //Получение всех подзадач эпика

    private void updateEpicStatus(int id){
         if(epics.containsKey(id)) { //Проверяем наличие эпика с нужным ID
              Epic epic = epics.get(id); //Получаем эпик по ID
              ArrayList<Subtask> subtaskToEpic = getAllSubtasksToEpic(id); //Получаем все подзадачи эпика
              if (subtaskToEpic != null) { //Проверяем наличие подзадач
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
              } else
                  epic.setStatus(TaskStatus.NEW); //Если подзадач нет, то статус NEW
         }
    } //Обновление статуса эпика
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
