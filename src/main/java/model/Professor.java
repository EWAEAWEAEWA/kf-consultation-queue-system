package model;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private List<String> teachingSubjects;
    private boolean available;

    public Professor(String id, String username, String password, String name, String email) {
        super(id, username, password, name, email);
        this.teachingSubjects = new ArrayList<>();
        this.available = true;
    }

    public void addTeachingSubject(String subjectId) {
        if (!teachingSubjects.contains(subjectId)) {
            teachingSubjects.add(subjectId);
        }
    }

    public void removeTeachingSubject(String subjectId) {
        teachingSubjects.remove(subjectId);
    }

    public List<String> getTeachingSubjects() {
        return new ArrayList<>(teachingSubjects);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getDetailedInfo() {
        return getName() + "\nSubjects:\n- " + 
               String.join("\n- ", teachingSubjects);
    }

    public String getSubjectsDisplay() {
        return String.join(", ", teachingSubjects);
    }
} 