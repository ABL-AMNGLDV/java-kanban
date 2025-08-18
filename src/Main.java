public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Task t1 = manager.createTask(new TaskImplementation("Переезд", "Собрать коробки", Status.NEW));
        Task t2 = manager.createTask(new TaskImplementation("Купить билеты", "СПб -> МСК", Status.IN_PROGRESS));

        Epic e1 = manager.createEpic(new Epic("Сделать трекер", "Бэкенд и фронтенд"));
        Subtask s11 = manager.createSubtask(new Subtask("Скелет классов", "Task/Epic/Subtask", Status.NEW, e1.getId()));
        Subtask s12 = manager.createSubtask(new Subtask("Менеджер", "CRUD + правила", Status.NEW, e1.getId()));

        Epic e2 = manager.createEpic(new Epic("Переезд офиса", "Организация процесса"));
        Subtask s21 = manager.createSubtask(new Subtask("Найти грузчиков", "3 человека", Status.DONE, e2.getId()));

        System.out.println("=== Стартовые списки ===");
        System.out.println("Tasks: " + manager.getAllTasks());
        System.out.println("Epics: " + manager.getAllEpics());
        System.out.println("Subtasks: " + manager.getAllSubtasks());

        System.out.println("\n=== Проверка статусов эпиков ===");
        System.out.println("Epic e1: " + manager.getEpicById(e1.getId())); // без прогресса -> NEW
        System.out.println("Epic e2: " + manager.getEpicById(e2.getId())); // единственная DONE -> DONE

        System.out.println("\n=== Меняем статусы подзадач эпика e1 ===");
        manager.updateSubtask(new Subtask(s11.getId(), s11.getTitle(), s11.getDescription(), Status.IN_PROGRESS, e1.getId()));
        System.out.println("Epic e1 после IN_PROGRESS одной сабы: " + manager.getEpicById(e1.getId()));

        manager.updateSubtask(new Subtask(s11.getId(), s11.getTitle(), s11.getDescription(), Status.DONE, e1.getId()));
        manager.updateSubtask(new Subtask(s12.getId(), s12.getTitle(), s12.getDescription(), Status.DONE, e1.getId()));
        System.out.println("Epic e1 после всех DONE: " + manager.getEpicById(e1.getId()));

        System.out.println("\n=== Удаляем задачу и эпик ===");
        manager.deleteTaskById(t2.getId());
        manager.deleteEpicById(e2.getId());

        System.out.println("Tasks: " + manager.getAllTasks());
        System.out.println("Epics: " + manager.getAllEpics());
        System.out.println("Subtasks: " + manager.getAllSubtasks());
    }
}
