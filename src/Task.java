
// --- Базовая задача ---
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class Task {
    protected int id;                 // уникальный для всех типов
    protected String title;
    protected String description;
    protected Status status;

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Конструктор для обновления/восстановления по id
    public Task(int id, String title, String description, Status status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Геттеры/сеттеры (id сеттер — пакетный/публичный по задаче)
    public int getId() { return id; }
    void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
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



