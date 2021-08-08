package model;

public class Status {

    private Boolean completed;

    private int chapter;

    public Status(int chapter, boolean completed) {
        this.completed = completed;
        this.chapter = chapter;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean c) {
        this.completed = c;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int c) {
        this.chapter = c;
    }

    public String toString() {
        if (completed) {
            return "Completed";
        } else {
            return ("c" + getChapter());
        }
    }

}
