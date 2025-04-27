package util;

import model.*;
import controller.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class SystemInitializer {
    private UserController userController;
    private AppointmentController appointmentController;
    private NotificationController notificationController;

    public SystemInitializer(UserController userController, AppointmentController appointmentController, NotificationController notificationController) {
        this.userController = userController;
        this.appointmentController = appointmentController;
        this.notificationController = notificationController;
    }

    public void initializeSystem() {
        // Initialize Subjects
        List<String> subjects = Arrays.asList(
            "CS 111 - Introduction to Computing",
            "CS 112 - Computer Programming 1",
            "CS 113 - Computer Programming 2",
            "CS 211 - Data Structures",
            "CS 212 - Operating Systems",
            "CS 213 - Object-Oriented Programming",
            "CS 214 - Systems Analysis and Design",
            "CS 311 - Software Engineering",
            "CS 312 - Web Development",
            "CS 313 - Database Management Systems"
        );

        // Initialize Professors with their subjects
        String profGarcia = userController.registerProfessor(
            "pgarcia", "pass123", "Dr. Paolo Garcia", "pgarcia@university.edu.ph");
        String profSantos = userController.registerProfessor(
            "msantos", "pass123", "Dr. Maria Santos", "msantos@university.edu.ph");
        String profReyes = userController.registerProfessor(
            "jreyes", "pass123", "Dr. Juan Reyes", "jreyes@university.edu.ph");

        // Assign subjects to professors
        Professor profG = (Professor) userController.getUserById(profGarcia);
        profG.addTeachingSubject(subjects.get(0)); // Intro to Computing
        profG.addTeachingSubject(subjects.get(1)); // Programming 1
        profG.addTeachingSubject(subjects.get(2)); // Programming 2

        Professor profS = (Professor) userController.getUserById(profSantos);
        profS.addTeachingSubject(subjects.get(3)); // Data Structures
        profS.addTeachingSubject(subjects.get(4)); // Operating Systems
        profS.addTeachingSubject(subjects.get(5)); // OOP

        Professor profR = (Professor) userController.getUserById(profReyes);
        profR.addTeachingSubject(subjects.get(6)); // Systems Analysis
        profR.addTeachingSubject(subjects.get(7)); // Software Engineering
        profR.addTeachingSubject(subjects.get(8)); // Web Development

        // Initialize Counselors
        String counselorLim = userController.registerCounselor(
            "alim", "pass123", "Ms. Ana Lim", "alim@university.edu.ph");
        String counselorCruz = userController.registerCounselor(
            "bcruz", "pass123", "Mr. Ben Cruz", "bcruz@university.edu.ph");

        // Initialize Students
        String student1 = userController.registerStudent(
            "jdelacruz", "pass123", "Juan Dela Cruz", "jdelacruz@student.university.edu.ph");
        String student2 = userController.registerStudent(
            "msilva", "pass123", "Maria Silva", "msilva@student.university.edu.ph");
        String student3 = userController.registerStudent(
            "raquino", "pass123", "Ramon Aquino", "raquino@student.university.edu.ph");

        // Enroll students in subjects
        Student s1 = (Student) userController.getUserById(student1);
        s1.enrollSubject(subjects.get(0));
        s1.enrollSubject(subjects.get(1));
        s1.enrollSubject(subjects.get(3));

        Student s2 = (Student) userController.getUserById(student2);
        s2.enrollSubject(subjects.get(1));
        s2.enrollSubject(subjects.get(2));
        s2.enrollSubject(subjects.get(4));

        Student s3 = (Student) userController.getUserById(student3);
        s3.enrollSubject(subjects.get(2));
        s3.enrollSubject(subjects.get(3));
        s3.enrollSubject(subjects.get(5));

        // Create sample appointments
        LocalDateTime now = LocalDateTime.now();
        
        // Today's appointments
        createSampleAppointment(student1, profGarcia, now.plusHours(1), 
            "CS 111 - Help with Java Basics", "Need help with loops and arrays");
        createSampleAppointment(student2, profSantos, now.plusHours(2), 
            "CS 212 - Project Consultation", "Discussion about final project implementation");
        createSampleAppointment(student3, profReyes, now.plusHours(3), 
            "CS 311 - Software Design Review", "Review of system architecture");

        // Tomorrow's appointments
        LocalDateTime tomorrow = now.plusDays(1);
        createSampleAppointment(student1, profSantos, tomorrow.withHour(10), 
            "CS 211 - Data Structures Help", "Binary tree implementation questions");
        createSampleAppointment(student2, profReyes, tomorrow.withHour(14), 
            "CS 312 - Web Project Discussion", "Frontend framework selection");

        // Counseling appointments
        createSampleAppointment(student3, counselorLim, tomorrow.withHour(11), 
            "Academic Planning", "Course selection for next semester");

        // Add debug output
        System.out.println("Initialization complete");
        System.out.println("Created professors: " + userController.getAllProfessors().size());
        System.out.println("Created students: " + userController.getAllStudents().size());
        appointmentController.printDebugInfo();
    }

    private void createSampleAppointment(String studentId, String professorId, 
                                       LocalDateTime dateTime, String subject, String description) {
        try {
            String appointmentId = appointmentController.createAppointment(
                studentId,
                professorId,
                dateTime,
                Duration.ofMinutes(30),
                subject,
                description
            );
            System.out.println("Created appointment: " + appointmentId + " for professor: " + professorId);
        } catch (Exception e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 