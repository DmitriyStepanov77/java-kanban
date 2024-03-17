import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    public HashMap<Integer, Task> getTasksList() {
        return taskList;
    } //Получение всех задач

    public HashMap<Integer, Epic> getEpicList() {
        return epicList;
    } //Получение всех эпиков

    public HashMap<Integer, Subtask> getSubtaskList() {
        return subtaskList;
    } //Получение всех подзадач

    public void add(Task task) {
        taskList.put(task.getId(),task);
    } //Добавление задачи

    public void add(Epic epic) {
        epicList.put(epic.getId(),epic);
        updateEpicStatus(epic.getId());
    } //Добавление эпика

    public void add(Subtask subtask) {
        subtaskList.put(subtask.getId(),subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
    } //Добавление подзадачи

    public void update(Task task) {
        taskList.replace(task.getId(), task);
    } //Обновление задачи

    public void update(Epic epic) {
        epicList.replace(epic.getId(), epic);
        updateEpicStatus(epic.getId());
    } //Обновление эпика

    public void update(Subtask subtask) {
        subtaskList.replace(subtask.getId(), subtask);
        if (subtask.getEpicId() != 0) {
            updateEpicStatus(subtask.getEpicId());
        }
    } //Обновление подзадачи

    public Task getTask(int key) {
        if(taskList.containsKey(key))
            return taskList.get(key);
        else
            return null;
    } //Получение задачи

    public Epic getEpic(int key) {
        if(epicList.containsKey(key))
            return epicList.get(key);
        else
            return null;
    } //Получение эпика

    public Subtask getSubtask(int key) {
        if(subtaskList.containsKey(key))
            return subtaskList.get(key);
        else
            return null;
    } //Получение подзадачи

    public void removeAllTask() {
        taskList.clear();
    } //Удаление всех задач

    public void removeAllEpic() {
        epicList.clear();
    } //Удаление всех эпиков

    public void removeAllSubtask() {
        subtaskList.clear();
    } //Удаление всех подзадач

    public void removeTask(int key) {
        if(taskList.containsKey(key))
            taskList.remove(key);
    } //Удаление задачи

    public void removeEpic(int key) {
        if(epicList.containsKey(key))
            epicList.remove(key);
    } //Удаление эпика

    public void removeSubtask(int key) {
        if(subtaskList.containsKey(key))
            subtaskList.remove(key);
    } //Удаление подзадачи

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

}
