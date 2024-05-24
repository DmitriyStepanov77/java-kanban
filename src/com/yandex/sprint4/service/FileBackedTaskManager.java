package com.yandex.sprint4.service;

import com.yandex.sprint4.model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    static Path fileName;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileManger = new FileBackedTaskManager(new InMemoryHistoryManager());
        fileName = Path.of(file.getName());
        if (Files.exists(fileName)) {
            try (FileReader reader = new FileReader(String.valueOf(fileName), StandardCharsets.UTF_8)) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                while (bufferedReader.ready()) {
                    String str = bufferedReader.readLine();
                    if (str.indexOf(";") == -1) {
                        continue;
                    }
                    String[] split = str.split(";");
                    if ((split.length == 7) & ((TaskType.valueOf(split[0]) == TaskType.TASK)
                            || (TaskType.valueOf(split[0]) == TaskType.SUBTASK))
                            || ((split.length == 8) & (TaskType.valueOf(split[0]) == TaskType.EPIC))) {
                        switch (TaskType.valueOf(split[0])) {
                            case TaskType.TASK -> fileManger.tasks.put(toTask(split).getId(), toTask(split));
                            case TaskType.SUBTASK ->
                                    fileManger.subtasks.put(toSubtask(split).getId(), toSubtask(split));
                            case TaskType.EPIC -> fileManger.epics.put(toEpic(split).getId(), toEpic(split));
                        }
                    }
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e);
            }
        }
        return fileManger;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeTask(int key) {
        super.removeTask(key);
        save();
    }

    @Override
    public void removeEpic(int key) {
        super.removeEpic(key);
        save();
    }

    @Override
    public void removeSubtask(int key) {
        super.removeSubtask(key);
        save();
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(fileName), StandardCharsets.UTF_8))) {
            if (Files.notExists(fileName))
                Files.createFile(fileName);
            for (Integer key : tasks.keySet()) {
                writer.write(toStringFromCSVTask(tasks.get(key)) + "\n");
            }
            for (Integer key : subtasks.keySet()) {
                writer.write(toStringFromCSVSubtask(subtasks.get(key)) + "\n");
            }
            for (Integer key : epics.keySet()) {
                writer.write(toStringFromCSVEpic(epics.get(key)) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private String toStringFromCSVTask(Task task) {
        return TaskType.TASK + ";" + task.getId() + ";" + task.getName() + ';' + task.getDescription() + ";"
                + task.getStatus() + ";" + task.getDuration() + ";" + task.getStartTime();
    }

    private String toStringFromCSVSubtask(Subtask subtask) {
        return TaskType.SUBTASK + ";" + subtask.getId() + ";" + subtask.getName() + ';' + subtask.getDescription() + ";"
                + subtask.getStatus() + ";" + subtask.getEpicId() + ";" + subtask.getDuration() + ";"
                + subtask.getStartTime();
    }

    private String toStringFromCSVEpic(Epic epic) {
        return TaskType.EPIC + ";" + epic.getId() + ";" + epic.getName() + ';' + epic.getDescription() + ";"
                + epic.getStatus() + ";" + epic.getSubtasksId();
    }

    private static Task toTask(String[] values) {
        Duration duration = values[5].equals("null") ? null : Duration.parse(values[5]);
        LocalDateTime startTime = values[6].equals("null") ? null : LocalDateTime.parse(values[6]);
        Task task = new Task(values[2], values[3], TaskStatus.valueOf(values[4]),
                duration, startTime);
        task.setId(Integer.parseInt(values[1]));
        return task;
    }

    private static Subtask toSubtask(String[] values) {
        Duration duration = values[6].equals("null") ? null : Duration.parse(values[6]);
        LocalDateTime startTime = values[7].equals("null") ? null : LocalDateTime.parse(values[7]);
        Subtask subtask = new Subtask(values[2], values[3], TaskStatus.valueOf(values[4]), Integer.parseInt(values[5]),
                duration, startTime);
        subtask.setId(Integer.parseInt(values[1]));
        return subtask;
    }

    private static Epic toEpic(String[] values) {
        String temp = values[5].replace("[", "").replace("]", "");
        String[] split = temp.split(", ");
        ArrayList<Integer> subtasksId = new ArrayList<>();
        for (String o : split)
            subtasksId.add(Integer.parseInt(o));
        Epic epic = new Epic(values[2], values[3], subtasksId);
        epic.setId(Integer.parseInt(values[1]));
        return epic;
    }
}
