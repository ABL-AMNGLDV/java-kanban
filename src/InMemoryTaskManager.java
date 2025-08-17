// --- Менеджер в памяти ---
import java.util.*;

class InMemoryTaskManager {
    private int idSeq = 1;

    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    private int nextId() {
        return idSeq++;
    }

    // ---------- Task ----------
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Task createTask(Task task) {
        int id = nextId();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public Task updateTask(Task task) {
        // Полная замена по id
        if (!tasks.containsKey(task.getId())) return null;
        tasks.put(task.getId(), task);
        return task;
    }

    public Task deleteTaskById(int id) {
        return tasks.remove(id);
    }

    // ---------- Epic ----------
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void removeAllEpics() {
        for (Epic epic : epics.values()) {
            for (int subId : epic.getSubtaskIds()) {
                subtasks.remove(subId);
            }
        }
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Epic createEpic(Epic epic) {
        int id = nextId();
        epic.setId(id);
        epics.put(id, epic);
        // статус эпика вычисляется; без подзадач — NEW
        recalcEpicStatus(id);
        return epic;
    }

    public Epic updateEpic(Epic epic) {
        // Меняем только базовые поля (title/description), состав сабтасков/статус — под контролем менеджера
        Epic existing = epics.get(epic.getId());
        if (existing == null) return null;
        existing.title = epic.title;
        existing.description = epic.description;
        recalcEpicStatus(existing.getId());
        return existing;
    }

    public Epic deleteEpicById(int id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (int subId : new ArrayList<>(removed.getSubtaskIds())) {
                subtasks.remove(subId);
            }
        }
        return removed;
    }

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


    // ---------- Subtask ----------
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void removeAllSubtasks() {
        // Удаляем ссылки из эпиков
        for (Epic epic : epics.values()) {
            epic.clearSubtasks();
            recalcEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    public Subtask createSubtask(Subtask subtask) {
        // Проверим, что эпик существует
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) throw new IllegalArgumentException("Epic " + subtask.getEpicId() + " not found");
        int id = nextId();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        recalcEpicStatus(epic.getId());
        return subtask;
    }

    public Subtask updateSubtask(Subtask subtask) {
        Subtask existing = subtasks.get(subtask.getId());
        if (existing == null) return null;
        if (existing.getEpicId() != subtask.getEpicId())
            throw new IllegalArgumentException("Нельзя переносить подзадачу между эпиками через update");
        subtasks.put(subtask.getId(), subtask);
        recalcEpicStatus(subtask.getEpicId());
        return subtask;
    }

    public Subtask deleteSubtaskById(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                recalcEpicStatus(epic.getId());
            }
        }
        return removed;
    }

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
                epic.status = Status.IN_PROGRESS; // найден IN_PROGRESS
                return;
            }

            if (hasNew && hasDone) {
                epic.status = Status.IN_PROGRESS; // есть и NEW и DONE
                return;
            }
        }

        // если все NEW
        if (hasNew && !hasDone) epic.status = Status.NEW;
            // если все DONE
        else if (!hasNew && hasDone) epic.status = Status.DONE;
    }

}
