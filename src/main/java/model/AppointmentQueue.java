package model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.*;

public class AppointmentQueue {
    private String professorId;
    private Queue<Appointment> regularQueue;
    private PriorityQueue<Appointment> priorityQueue;
    private Map<String, Appointment> allAppointments;
    private Duration averageConsultationTime;

    public AppointmentQueue(String professorId) {
        this.professorId = professorId;
        this.regularQueue = new LinkedList<>();
        this.priorityQueue = new PriorityQueue<>((a1, a2) -> {
            if (a1.isPriority() != a2.isPriority()) {
                return a1.isPriority() ? -1 : 1;
            }
            return a1.getScheduledTime().compareTo(a2.getScheduledTime());
        });
        this.allAppointments = new HashMap<>();
        this.averageConsultationTime = Duration.ofMinutes(30); // default 30 minutes
    }

    public void addAppointment(Appointment appointment) {
        if (appointment.isPriority()) {
            priorityQueue.offer(appointment);
        } else {
            regularQueue.offer(appointment);
        }
        allAppointments.put(appointment.getId(), appointment);
    }

    public Appointment getNextAppointment() {
        Appointment next = priorityQueue.isEmpty() ? regularQueue.poll() : priorityQueue.poll();
        if (next != null) {
            allAppointments.remove(next.getId());
        }
        return next;
    }

    public Duration getEstimatedWaitTime() {
        return averageConsultationTime.multipliedBy(getQueueSize());
    }

    public int getQueueSize() {
        return priorityQueue.size() + regularQueue.size();
    }

    public void removeAppointment(String appointmentId) {
        Appointment appointment = allAppointments.get(appointmentId);
        if (appointment != null) {
            priorityQueue.remove(appointment);
            regularQueue.remove(appointment);
            allAppointments.remove(appointmentId);
        }
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(allAppointments.values());
    }

    public String getProfessorId() {
        return professorId;
    }

    public void updateAverageConsultationTime(Duration newAverage) {
        this.averageConsultationTime = newAverage;
    }
} 