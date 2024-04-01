package com.yandex.sprint4.service;

import com.yandex.sprint4.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getTasksList() {
        return new ArrayList<>(taskList.values());
    } //Получение всех задач

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(epicList.values());
    } //Получение всех эпиков

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskList.values());
    } //Получение всех подзадач

    @Override
    public void add(Task task) {
        task.setId(id);
        taskList.put(task.getId(),task);
        id++;
    } //Добавление задачи

    @Override
    public void add(Epic epic) {
        epic.setId(id);
        epicList.put(epic.getId(),epic);
        updateEpicStatus(epic.getId());
        id++;
    } //Добавление эпика

    @Override
    public void add(Subtask subtask) {
        subtask.setId(id);
        subtaskList.put(subtask.getId(),subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
        id++;
    } //Добавление подзадачи

    @Override
    public void update(Task task) {
        taskList.replace(task.getId(), task);
    } //Обновление задачи

    @Override
    public void update(Epic epic) {
        epicList.replace(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    } //Обновление эпика

    @Override
    public void update(Subtask subtask) {
        subtaskList.replace(subtask.getId(), subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
    } //Обновление подзадачи

    @Override
    public Task getTask(int key) {
        if(taskList.containsKey(key)) {
            historyManager.add(taskList.get(key));
            return taskList.get(key);
        }
        else
            return null;
    } //Получение задачи

    @Override
    public Epic getEpic(int key) {
        if(epicList.containsKey(key)) {
            historyManager.add(epicList.get(key));
            return epicList.get(key);
        }
        else
            return null;
    } //Получение эпика

    @Override
    public Subtask getSubtask(int key) {
        if(subtaskList.containsKey(key)) {
            historyManager.add(subtaskList.get(key));
            return subtaskList.get(key);
        }
        else
            return null;
    } //Получение подзадачи

    @Override
    public void removeAllTask() {
        taskList.clear();
    } //Удаление всех задач

    @Override
    public void removeAllEpic() {
        epicList.clear();
        subtaskList.clear();
    } //Удаление всех эпиков

    @Override
    public void removeAllSubtask() {
        subtaskList.clear();
        for (Integer i : epicList.keySet()) {
            updateEpicStatus(i);
        }
    } //Удаление всех подзадач

    @Override
    public void removeTask(int key) {
        if(taskList.containsKey(key))
            taskList.remove(key);
    } //Удаление задачи

    @Override
    public void removeEpic(int key) {
        if(epicList.containsKey(key)) {
            Epic epic = epicList.get(key); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) { //Проверяем наличие подзадач для эпика
                System.out.println(subtaskEpicList);
                for (Integer o : subtaskEpicList) {
                   if (o != null && subtaskList.containsKey(o))
                       subtaskList.remove(o);
                }
            }
            epicList.remove(key);
        }
    } //Удаление эпика

    @Override
    public void removeSubtask(int key) {
        if(subtaskList.containsKey(key)) {
            Integer epicId = subtaskList.get(key).getEpicId();
            if(epicList.containsKey(epicId)) {
                Epic epic = epicList.get(epicId);
                epic.removeSubtask(key);
                updateEpicStatus(epicId);
            }
            subtaskList.remove(key);
        }
    } //Удаление подзадачи
    @Override
    public ArrayList<Subtask> getAllSubtaskToEpic(int epicId) {
        if(epicList.containsKey(epicId)) { //Проверяем, есть ли в списке эпиков эпик с нужным ID
            Epic epic = epicList.get(epicId); //Получаем эпик по ID
            ArrayList<Integer> subtaskEpicList = epic.getSubtasksId(); //Создаем список ID подзадач эпика
            if (subtaskEpicList != null) { //Проверяем наличие подзадач для эпика
                ArrayList<Subtask> subtasksToEpic = new ArrayList<>(); //Создаем новый список, в который запишем подзадачи
                for (Integer o : subtaskEpicList)
                    subtasksToEpic.add(subtaskList.get(o)); //Дабавляем из хэш-карты подзадач задачи с нужными ID
                return subtasksToEpic;
            } else
                return null;
        } else
            return null;
    } //Получение всех подзадач эпика

    private void updateEpicStatus(int id){
         if(epicList.containsKey(id)) { //Проверяем наличие эпика с нужным ID
              Epic epic = epicList.get(id); //Получаем эпик по ID
              ArrayList<Subtask> subtaskToEpic = getAllSubtaskToEpic(id); //Получаем все подзадачи эпика
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
