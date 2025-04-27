package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import controller.*;
import model.*;
import java.util.Arrays;

public class QueueStatusView extends JPanel {
    private AppointmentController appointmentController;
    private UserController userController;
    private JTable queueTable;
    private DefaultTableModel tableModel;
    private Timer refreshTimer;

    public QueueStatusView(AppointmentController appointmentController, UserController userController) {
        this.appointmentController = appointmentController;
        this.userController = userController;
        setupUI();
        startAutoRefresh();
    }

    private void setupUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Queue Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // Add refresh button
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> refreshQueueStatus());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // Queue Table
        String[] columnNames = {"Position", "Student", "Time", "Duration", "Subject", "Priority", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Only actions column is editable
            }
        };

        queueTable = new JTable(tableModel);
        queueTable.setRowHeight(35);
        queueTable.getTableHeader().setReorderingAllowed(false);
        
        // Custom renderer for the Actions column
        queueTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        queueTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(appointmentController));

        JScrollPane scrollPane = new JScrollPane(queueTable);
        add(scrollPane, BorderLayout.CENTER);

        // Statistics Panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);

        refreshQueueStatus();
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Create stat boxes
        addStatBox(statsPanel, "Current Queue Size", "0");
        addStatBox(statsPanel, "Average Wait Time", "0 min");
        addStatBox(statsPanel, "Completed Today", "0");

        return statsPanel;
    }

    private void addStatBox(JPanel container, String title, String value) {
        JPanel statBox = new JPanel(new BorderLayout(5, 5));
        statBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));

        statBox.add(titleLabel, BorderLayout.NORTH);
        statBox.add(valueLabel, BorderLayout.CENTER);
        
        container.add(statBox);
    }

    private void refreshQueueStatus() {
        tableModel.setRowCount(0);
        String professorId = userController.getCurrentUser().getId();
        List<Appointment> appointments = appointmentController.getProfessorAppointments(professorId);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        int position = 1;
        for (Appointment apt : appointments) {
            User student = userController.getUserById(apt.getStudentId());
            String studentName = student != null ? student.getName() : "Unknown Student";
            
            tableModel.addRow(new Object[]{
                position++,
                studentName,
                apt.getScheduledTime().format(formatter),
                apt.getDuration().toMinutes() + " min",
                apt.getSubject(),
                apt.isPriority() ? "Yes" : "No",
                apt.getStatus().toString(),
                apt // Pass the appointment object for the action panel
            });
        }

        updateStatistics(appointments);
    }

    private JPanel createActionPanel(Appointment apt) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JButton startButton = new JButton("Start");
        JButton completeButton = new JButton("Complete");
        JButton priorityButton = new JButton(apt.isPriority() ? "Unprioritize" : "Prioritize");
        
        startButton.addActionListener(e -> {
            appointmentController.startAppointment(apt.getId());
            refreshQueueStatus();
        });
        
        completeButton.addActionListener(e -> {
            appointmentController.completeAppointment(apt.getId());
            refreshQueueStatus();
        });
        
        priorityButton.addActionListener(e -> {
            if (apt.isPriority()) {
                appointmentController.removePriority(apt.getId());
            } else {
                appointmentController.markAsPriority(apt.getId());
            }
            refreshQueueStatus();
        });
        
        panel.add(startButton);
        panel.add(completeButton);
        panel.add(priorityButton);
        
        return panel;
    }

    private void updateStatistics(List<Appointment> appointments) {
        // Update statistics (implement your logic here)
        int queueSize = appointments.size();
        int avgWaitTime = calculateAverageWaitTime(appointments);
        int completedToday = countCompletedToday(appointments);

        // Update stat boxes
        updateStatBox(0, String.valueOf(queueSize));
        updateStatBox(1, avgWaitTime + " min");
        updateStatBox(2, String.valueOf(completedToday));
    }

    private void updateStatBox(int index, String value) {
        JPanel statsPanel = (JPanel) getComponent(2);
        JPanel statBox = (JPanel) statsPanel.getComponent(index);
        JLabel valueLabel = (JLabel) statBox.getComponent(1);
        valueLabel.setText(value);
    }

    private String getUserName(String userId) {
        User user = userController.getUserById(userId);
        return user != null ? user.getName() : "Unknown";
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(30000, e -> refreshQueueStatus()); // Refresh every 30 seconds
        refreshTimer.start();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }

    private int calculateAverageWaitTime(List<Appointment> appointments) {
        // Implement wait time calculation logic
        return 15; // Placeholder
    }

    private int countCompletedToday(List<Appointment> appointments) {
        // Implement completed appointments count logic
        return (int) appointments.stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
            .count();
    }
} 