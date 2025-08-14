// --- Подзадача: принадлежит одному эпику ---
class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status, int epicId) {
        super(id, title, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() { return epicId; }

    @Override
    public String toString() {
        return super.toString() + " -> epicId=" + epicId;
    }
}