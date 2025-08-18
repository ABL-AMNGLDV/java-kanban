import java.util.List;

public interface TaskManager {
    // Task
    Task createTask(Task task);
    Task updateTask(Task task);
    Task getTaskById(int id);
    Task deleteTaskById(int id);
    List<Task> getAllTasks();

    // Epic
    Epic createEpic(Epic epic);
    Epic updateEpic(Epic epic);
    Epic getEpicById(int id);
    Epic deleteEpicById(int id);
    List<Epic> getAllEpics();

    // Subtask
    Subtask createSubtask(Subtask subtask);
    Subtask updateSubtask(Subtask subtask);
    Subtask getSubtaskById(int id);
    Subtask deleteSubtaskById(int id);
    List<Subtask> getAllSubtasks();
    List<Subtask> getSubtasksOfEpic(int epicId);

    // Новое: история просмотров
    List<Task> getHistory();
}