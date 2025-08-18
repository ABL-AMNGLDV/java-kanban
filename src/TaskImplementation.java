import java.util.Objects;

class TaskImplementation implements Task {
    protected int id;                 // уникальный для всех типов
    protected String title;
    protected String description;
    protected Status status;

    public TaskImplementation(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public TaskImplementation(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskImplementation)) return false;
        TaskImplementation task = (TaskImplementation) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s{id=%d, title='%s', status=%s}",
                this.getClass().getSimpleName(), id, title, status);
    }
}



