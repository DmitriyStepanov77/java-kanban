import java.util.Objects;

public class Task {
    private static int countTaskAllTime = 0;
    private String Name;
    private int id;
    private String Description;
    protected TaskStatus status;

    public Task(String name, String description, TaskStatus status) {
        Name = name;
        Description = description;
        this.status = status;
        countTaskAllTime += 1;
        id = countTaskAllTime;
    }

    public Task(String name, String description) { //Констурктор для использования в эпике(без записи статуса)
        Name = name;
        Description = description;
        countTaskAllTime += 1;
        id = countTaskAllTime;
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return Description;
    }

    public TaskStatus getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "Task{" +
                "Name='" + Name + '\'' +
                ", id=" + id +
                ", Description='" + Description + '\'' +
                ", status=" + status +
                '}';
    }
}
