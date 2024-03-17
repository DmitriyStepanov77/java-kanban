import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> subtasksId;

    public Epic(String name, String description, ArrayList<Integer> subtaskId) { //Конструктор для нескольких ID подзадач
        super(name, description);
        this.subtasksId = new ArrayList<>();
        subtasksId.addAll(subtaskId);
    }

    public Epic(String name, String description, int subtaskId) { //Конструктор для одного ID подзадачи
        super(name, description);
        this.subtasksId = new ArrayList<>();
        subtasksId.add(subtaskId);
    }

    public Epic(String name, String description) { //Конструктор без ID подзадач
        super(name, description);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setStatus(TaskStatus status) {
        super.status = status;
    }
}
