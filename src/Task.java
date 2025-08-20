public interface Task {
        int getId();
        void setId(int id);
        String getTitle();
        String getDescription();
        Status getStatus();
        void setStatus(Status status);
}