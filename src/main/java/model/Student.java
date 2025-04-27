package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private List<String> enrolledSubjects;

    public Student(String id, String username, String password, String name, String email) {
        super(id, username, password, name, email);
        this.enrolledSubjects = new ArrayList<>();
    }

    public void enrollSubject(String subjectId) {
        enrolledSubjects.add(subjectId);
    }

    public List<String> getEnrolledSubjects() {
        return enrolledSubjects;
    }
} 