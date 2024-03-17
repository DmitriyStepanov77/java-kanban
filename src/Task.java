import java.util.Objects;

public class Task {
    private static int countTaskAllTime;
    private String Name;
    private int id;
    private String Description;
    private TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        Name = name;
        Description = description;
        this.status = status;
        countTaskAllTime += 1;
        id = countTaskAllTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(Name, task.Name) && Objects.equals(Description, task.Description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
