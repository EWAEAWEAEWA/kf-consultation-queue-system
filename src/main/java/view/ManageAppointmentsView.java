package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import controller.AppointmentController;
import controller.UserController;
import model.Appointment;
import model.User;

public class ManageAppointmentsView extends JPanel {
    private List<Appointment> appointments;
    private AppointmentController appointmentController;
    private JTable appointmentsTable;
    private DefaultTableModel tableModel;
    private UserController userController;

    public ManageAppointmentsView(List<Appointment> appointments, AppointmentController appointmentController, UserController userController) {
        this.appointments = appointments;
        this.appointmentController = appointmentController;
        this.userController = userController;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Manage Appointments");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Time", "Student", "Subject", "Duration", "Status", "Priority", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Only actions column is editable
            }
        };

        appointmentsTable = new JTable(tableModel);
        appointmentsTable.setRowHeight(35);
        appointmentsTable.getTableHeader().setReorderingAllowed(false);

        // Add custom renderers and editors for action buttons
        appointmentsTable.getColumnModel().getColumn(6).setCellRenderer(new ActionButtonRenderer());
        appointmentsTable.getColumnModel().getColumn(6).setCellEditor(new ActionButtonEditor(appointmentController));

        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        loadAppointments();
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadAppointments());
        
        JComboBox<String> filterCombo = new JComboBox<>(new String[]{
            "All", "Pending", "In Progress", "Completed", "Cancelled"
        });
        filterCombo.addActionListener(e -> filterAppointments((String)filterCombo.getSelectedItem()));

        panel.add(refreshButton);
        panel.add(new JLabel("  Filter: "));
        panel.add(filterCombo);

        return panel;
    }

    private void loadAppointments() {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Appointment apt : appointments) {
            User student = userController.getUserById(apt.getStudentId());
            String studentName = student != null ? student.getName() : "Unknown Student";
            
            tableModel.addRow(new Object[]{
                apt.getScheduledTime().format(formatter),
                studentName,
                apt.getSubject(),
                apt.getDuration().toMinutes() + " min",
                apt.getStatus().toString(),
                apt.isPriority() ? "Yes" : "No",
                apt // Pass the appointment object for the action panel
            });
        }
    }

    private JPanel createActionPanel(Appointment apt) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        
        editButton.addActionListener(e -> showEditDialog(apt));
        deleteButton.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this appointment?", 
                "Confirm Delete", 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                appointmentController.cancelAppointment(apt.getId());
                loadAppointments();
            }
        });
        
        panel.add(editButton);
        panel.add(deleteButton);
        
        return panel;
    }

    private void showEditDialog(Appointment apt) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Appointment", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Add form fields for editing
        JTextField subjectField = new JTextField(apt.getSubject(), 20);
        JTextArea descriptionArea = new JTextArea(apt.getDescription(), 4, 20);
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(
            apt.getDuration().toMinutes(), 15, 120, 15));
        
        // Add fields to panel
        addFormField(panel, "Subject:", subjectField, gbc, 0);
        addFormField(panel, "Description:", new JScrollPane(descriptionArea), gbc, 1);
        addFormField(panel, "Duration (min):", durationSpinner, gbc, 2);
        
        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            appointmentController.updateAppointment(
                apt.getId(),
                subjectField.getText(),
                descriptionArea.getText(),
                Duration.ofMinutes((int)durationSpinner.getValue())
            );
            dialog.dispose();
            apt.setSubject(subjectField.getText());
            apt.setDescription(descriptionArea.getText());
            apt.setDuration(Duration.ofMinutes((int)durationSpinner.getValue()));
            dialog.dispose();
            loadAppointments();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        panel.add(buttonPanel, gbc);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void filterAppointments(String filter) {
        tableModel.setRowCount(0);
        List<Appointment> filtered = appointments.stream()
            .filter(apt -> filter.equals("All") || apt.getStatus().toString().equals(filter))
            .collect(Collectors.toList());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Appointment apt : filtered) {
            User student = userController.getUserById(apt.getStudentId());
            String studentName = student != null ? student.getName() : "Unknown Student";
            
            tableModel.addRow(new Object[]{
                studentName,
                apt.getScheduledTime().format(formatter),
                apt.getSubject(),
                apt.getDuration().toMinutes() + " min",
                apt.getStatus().toString(),
                apt.isPriority() ? "Yes" : "No",
                createActionPanel(apt)
            });
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
    }
}