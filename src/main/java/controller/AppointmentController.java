package controller;

import model.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentController {
    private Map<String, AppointmentQueue> professorQueues;
    private Map<String, AppointmentQueue> counselorQueues;
    private Map<String, Appointment> appointments;

    public AppointmentController() {
        this.professorQueues = new HashMap<>();
        this.counselorQueues = new HashMap<>();
        this.appointments = new HashMap<>();
    }

    public String createAppointment(String studentId, String professorId, LocalDateTime scheduledTime, 
                                  Duration duration, String subject, String description) {
        String appointmentId = generateAppointmentId();
        Appointment appointment = new Appointment(appointmentId, studentId, professorId, 
                                               scheduledTime, duration, subject, description);
        
        // Store in appointments map
        appointments.put(appointmentId, appointment);

        // Add to professor's queue
        AppointmentQueue queue = professorQueues.computeIfAbsent(professorId, 
            id -> new AppointmentQueue(id));
        queue.addAppointment(appointment);

        return appointmentId;
    }

    public void cancelAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            String professorId = appointment.getProfessorId();
            AppointmentQueue queue = professorQueues.get(professorId);
            if (queue != null) {
                queue.removeAppointment(appointmentId);
            }
            appointment.setStatus(AppointmentStatus.CANCELLED);
        }
    }

    public void markAsPriority(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.setPriority(true);
            // Re-queue with priority
            String professorId = appointment.getProfessorId();
            AppointmentQueue queue = professorQueues.get(professorId);
            if (queue != null) {
                queue.removeAppointment(appointmentId);
                queue.addAppointment(appointment);
            }
        }
    }

    public void removePriority(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.setPriority(false);
            // Re-queue without priority
            String professorId = appointment.getProfessorId();
            AppointmentQueue queue = professorQueues.get(professorId);
            if (queue != null) {
                queue.removeAppointment(appointmentId);
                queue.addAppointment(appointment);
            }
        }
    }

    public Duration getEstimatedWaitTime(String professorId) {
        AppointmentQueue queue = professorQueues.get(professorId);
        return queue != null ? queue.getEstimatedWaitTime() : Duration.ZERO;
    }

    public List<Appointment> getProfessorAppointments(String professorId) {
        AppointmentQueue queue = professorQueues.get(professorId);
        if (queue != null) {
            return queue.getAllAppointments();
        }
        return new ArrayList<>();
    }

    public List<Appointment> getStudentAppointments(String studentId) {
        return appointments.values().stream()
            .filter(apt -> apt.getStudentId().equals(studentId))
            .collect(Collectors.toList());
    }

    public List<Appointment> getCounselorAppointments(String counselorId) {
        return appointments.values().stream()
            .filter(apt -> apt.getProfessorId().equals(counselorId))
            .sorted(Comparator.comparing(Appointment::getScheduledTime))
            .collect(Collectors.toList());
    }

    public void startAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.IN_PROGRESS);
        }
    }

    public void completeAppointment(String appointmentId) {
        Appointment appointment = appointments.get(appointmentId);
        if (appointment != null) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
        }
    }

    public void updateAppointment(String id, String subject, String description, Duration duration) {
        Appointment apt = getAppointmentById(id);
        if (apt != null) {
            apt.setSubject(subject);
            apt.setDescription(description);
            apt.setDuration(duration);
        }
    }

    private boolean hasTimeConflict(Appointment newAppointment) {
        String professorId = newAppointment.getProfessorId();
        AppointmentQueue queue = professorQueues.get(professorId);
        
        if (queue != null) {
            return queue.getAllAppointments().stream()
                    .anyMatch(existing -> existing.conflictsWith(newAppointment));
        }
        return false;
    }

    private AppointmentQueue getOrCreateQueue(String professorId) {
        return professorQueues.computeIfAbsent(professorId, id -> new AppointmentQueue(id));
    }

    private String generateAppointmentId() {
        return "APT-" + UUID.randomUUID().toString();
    }

    // Add debug method
    public void printDebugInfo() {
        System.out.println("Total appointments: " + appointments.size());
        System.out.println("Professor queues: " + professorQueues.size());
        professorQueues.forEach((profId, queue) -> {
            System.out.println("Professor " + profId + " has " + queue.getQueueSize() + " appointments");
        });
    }

    private Appointment getAppointmentById(String id) {
        return appointments.get(id);
    }
} 