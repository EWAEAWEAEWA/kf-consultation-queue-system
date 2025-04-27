package controller;

import model.User;
import model.Student;
import model.Professor;
import model.Counselor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserController {
    private Map<String, User> users;
    private User currentUser;

    public UserController() {
        this.users = new HashMap<>();
        this.currentUser = null;
    }

    public boolean login(String username, String password) {
        Optional<User> user = users.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
        
        if (user.isPresent() && user.get().verifyPassword(password)) {
            currentUser = user.get();
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public String registerStudent(String username, String password, String name, String email) {
        String id = generateUserId();
        Student student = new Student(id, username, password, name, email);
        users.put(id, student);
        return id;
    }

    public String registerProfessor(String username, String password, String name, String email) {
        String id = generateUserId();
        Professor professor = new Professor(id, username, password, name, email);
        users.put(id, professor);
        return id;
    }

    public String registerCounselor(String username, String password, String name, String email) {
        String id = generateUserId();
        Counselor counselor = new Counselor(id, username, password, name, email);
        users.put(id, counselor);
        return id;
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }

    private String generateUserId() {
        return UUID.randomUUID().toString();
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public List<Professor> getAllProfessors() {
        return users.values().stream()
                   .filter(user -> user instanceof Professor)
                   .map(user -> (Professor) user)
                   .collect(Collectors.toList());
    }

    public List<Student> getAllStudents() {
        return users.values().stream()
                   .filter(user -> user instanceof Student)
                   .map(user -> (Student) user)
                   .collect(Collectors.toList());
    }

    public List<Counselor> getAllCounselors() {
        return users.values().stream()
                   .filter(user -> user instanceof Counselor)
                   .map(user -> (Counselor) user)
                   .collect(Collectors.toList());
    }
} 