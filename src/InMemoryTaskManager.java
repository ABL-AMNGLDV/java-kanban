import java.util.*;


class InMemoryTaskManager implements TaskManager {
    private int idSeq = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    // История просмотров
    private final List<Task> history = new LinkedList<>();
    private static final int HISTORY_LIMIT = 10;

    private int nextId() { return idSeq++; }

    // ---------- Task ----------
    @Override
    public List<Task> getAllTasks() { return new ArrayList<>(tasks.values()); }

    @Override
    public Task createTask(Task task) {
        int id = nextId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) return null;
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        addToHistory(task);
        return task;
    }

    @Override
    public Task deleteTaskById(int id) {
        Task removed = tasks.remove(id);
        if (removed != null) history.remove(removed);
        return removed;
    }

    // ---------- Epic ----------
    @Override
    public List<Epic> getAllEpics() { return new ArrayList<>(epics.values()); }

    @Override
    public Epic createEpic(Epic epic) {
        int id = nextId();
        epic.setId(id);
        epics.put(id, epic);
        recalcEpicStatus(id);
        return epic;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        Epic existing = epics.get(epic.getId());
        if (existing == null) return null;
        existing.title = epic.title;
        existing.description = epic.description;
        recalcEpicStatus(existing.getId());
        return existing;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        addToHistory(epic);
        return epic;
    }

    @Override
    public Epic deleteEpicById(int id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (int subId : new ArrayList<>(removed.getSubtaskIds())) {
                subtasks.remove(subId);
            }
            history.remove(removed);
        }
        return removed;
    }

    // ---------- Subtask ----------
    @Override
    public List<Subtask> getAllSubtasks() { return new ArrayList<>(subtasks.values()); }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return Collections.emptyList();
        List<Subtask> res = new ArrayList<>();
        for (int subId : epic.getSubtaskIds()) {
            Subtask s = subtasks.get(subId);
            if (s != null) res.add(s);
        }
        return res;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) throw new IllegalArgumentException("Epic " + subtask.getEpicId() + " not found");
        int id = nextId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        recalcEpicStatus(epic.getId());
        return subtask;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        Subtask existing = subtasks.get(subtask.getId());
        if (existing == null) return null;
        if (existing.getEpicId() != subtask.getEpicId())
            throw new IllegalArgumentException("Нельзя переносить подзадачу между эпиками через update");
        subtasks.put(subtask.getId(), subtask);
        recalcEpicStatus(subtask.getEpicId());
        return subtask;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        addToHistory(subtask);
        return subtask;
    }

    @Override
    public Subtask deleteSubtaskById(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                recalcEpicStatus(epic.getId());
            }
            history.remove(removed);
        }
        return removed;
    }

    // ---------- История просмотров ----------
    private void addToHistory(Task task) {
        if (task == null) return;
        history.add(task);
        if (history.size() > HISTORY_LIMIT) history.remove(0);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    // ---------- Вспомогательные методы ----------
    private void recalcEpicStatus(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) return;

        List<Integer> ids = epic.getSubtaskIds();
        if (ids.isEmpty()) {
            epic.status = Status.NEW;
            return;
        }

        boolean hasNew = false;
        boolean hasDone = false;

        for (int sid : ids) {
            Subtask s = subtasks.get(sid);
            if (s == null) continue;

            Status st = s.getStatus();
            if (st == Status.NEW) hasNew = true;
            else if (st == Status.DONE) hasDone = true;
            else {
                epic.status = Status.IN_PROGRESS;
                return;
            }

            if (hasNew && hasDone) {
                epic.status = Status.IN_PROGRESS;
                return;
            }
        }

        if (hasNew && !hasDone) epic.status = Status.NEW;
        else if (!hasNew && hasDone) epic.status = Status.DONE;
    }
}
