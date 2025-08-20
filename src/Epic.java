import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Epic extends TaskImplementation {
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

    void addSubtaskId(int id) { subtaskIds.add(id); }
    void removeSubtaskId(int id) {
        subtaskIds.remove(id);
    }
    void clearSubtasks() { subtaskIds.clear(); }


    @Override
    public void setStatus(Status status) {
    }
}