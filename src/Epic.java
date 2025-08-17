import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// --- Эпик: агрегирует подзадачи, статус вычисляется менеджером ---
class Epic extends Task {
    private final List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
    }

    public Epic(int id, String title, String description) {
        super(id, title, description, Status.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    // пакетный доступ — чтобы только менеджер мог управлять составом
    void addSubtaskId(int id) { subtaskIds.add(id); }
    void removeSubtaskId(int id) {
        subtaskIds.remove(id);
    }
    void clearSubtasks() { subtaskIds.clear(); }

    // Пользователь не должен напрямую менять статус эпика
    @Override
    public void setStatus(Status status) {
        // Игнорируем внешние попытки — статус высчитывается менеджером
    }
}
