import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        //Тесты
        Manager manager = new Manager();

        System.out.println("Поехали!");

        Task task1 = new Task("Задача 1", "Описание задачи 1", TaskStatus.NEW);
        Task task2 = new Task("Задача 2", "Описание задачи 2", TaskStatus.NEW);

        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1", new ArrayList<>(Arrays.asList(5,6)));
        Epic epic2 = new Epic("Эпик 2", "Описание эпика 1", 32);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи 1", TaskStatus.DONE, 3);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи 2", TaskStatus.DONE, 3);

        manager.add(task1);
        manager.add(task2);

        manager.add(epic1);
        manager.add(epic2);

        manager.add(subtask1);
        manager.add(subtask2);

        System.out.println("Список обычных задач:");
        System.out.println(manager.getTasksList());

        System.out.println("Свойства эпика 3:");
        System.out.println(manager.getEpic(3));

        System.out.println("Список подзадач эпика 3:");
        System.out.println(manager.getAllSubtaskToEpic(3));

        System.out.println("Удаление подзадачи эпика 3:");
        manager.removeSubtask(5);
        System.out.println(manager.getEpic(3));
        System.out.println(manager.getAllSubtaskToEpic(3));

    }
}
