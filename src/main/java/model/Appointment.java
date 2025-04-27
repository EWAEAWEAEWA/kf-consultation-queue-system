package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Appointment {
    private String id;
    private String studentId;
    private String professorId;
    private LocalDateTime scheduledTime;
    private Duration duration;
    private boolean isPriority;
    private AppointmentStatus status;
    private String subject;
    private String description;

    public Appointment(String id, String studentId, String professorId, LocalDateTime scheduledTime, 
                      Duration duration, String subject, String description) {
        this.id = id;
        this.studentId = studentId;
        this.professorId = professorId;
        this.scheduledTime = scheduledTime;
        this.duration = duration;
        this.subject = subject;
        this.description = description;
        this.isPriority = false;
        this.status = AppointmentStatus.PENDING;
    }

    // Getters
    public String getId() { return id; }
    public String getStudentId() { return studentId; }
    public String getProfessorId() { return professorId; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public Duration getDuration() { return duration; }
    public boolean isPriority() { return isPriority; }
    public AppointmentStatus getStatus() { return status; }
    public String getSubject() { return subject; }
    public String getDescription() { return description; }

    // Setters
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public void setPriority(boolean priority) { this.isPriority = priority; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setDuration(Duration duration) { this.duration = duration; }

    // Utility methods
    public LocalDateTime getEndTime() {
        return scheduledTime.plus(duration);
    }

    public boolean conflictsWith(Appointment other) {
        return !(this.getEndTime().isBefore(other.getScheduledTime()) || 
                other.getEndTime().isBefore(this.scheduledTime));
    }
} 