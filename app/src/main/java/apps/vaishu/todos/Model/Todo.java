package apps.vaishu.todos.Model;

public class Todo {

    private Integer id;
    private String title;
    private String notes;
    private Integer priority;
    private Boolean isComplete;
    private String date;

    public Todo(Integer id, String title, String notes, Integer priority, Boolean isComplete, String date) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.priority = priority;
        this.isComplete = isComplete;
        this.date = date;
    }

    public Todo(String title, String notes, Integer priority, Boolean isComplete, String date) {
        this.title = title;
        this.notes = notes;
        this.priority = priority;
        this.isComplete = isComplete;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getComplete() {
        return isComplete;
    }

    public void setComplete(Boolean complete) {
        isComplete = complete;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", notes='" + notes + '\'' +
                ", priority=" + priority +
                ", isComplete=" + isComplete +
                '}';
    }
}
