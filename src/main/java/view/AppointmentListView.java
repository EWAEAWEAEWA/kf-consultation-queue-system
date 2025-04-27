package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Appointment;
import java.util.List;
import java.time.format.DateTimeFormatter;
import model.User;
import controller.UserController;

public class AppointmentListView extends JPanel {
    private DefaultTableModel tableModel;
    private JTable appointmentsTable;
    private UserController userController;

    public AppointmentListView(List<Appointment> appointments, UserController userController) {
        this.userController = userController;
        setupUI();
        loadAppointments(appointments);
    }

    private void setupUI() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"Professor", "Date & Time", "Subject", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        appointmentsTable = new JTable(tableModel);
        
        add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
    }

    private void loadAppointments(List<Appointment> appointments) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Appointment apt : appointments) {
            User professor = userController.getUserById(apt.getProfessorId());
            String professorName = professor != null ? professor.getName() : "Unknown Professor";
            
            tableModel.addRow(new Object[]{
                professorName,
                apt.getScheduledTime().format(formatter),
                apt.getSubject(),
                apt.getStatus().toString()
            });
        }
    }
} 