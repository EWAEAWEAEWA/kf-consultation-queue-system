package view;

import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.List;
import controller.*;
import model.*;

public class AppointmentBookingView extends JPanel {
    private AppointmentController appointmentController;
    private UserController userController;
    private JComboBox<Professor> professorCombo;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;
    private JSpinner durationSpinner;
    private JTextField subjectField;
    private JTextArea descriptionArea;

    public AppointmentBookingView(AppointmentController appointmentController, UserController userController) {
        this.appointmentController = appointmentController;
        this.userController = userController;
        setupUI();
    }

    private void setupUI() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Book Appointment");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Form Panel with scroll
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Professor Selection with custom renderer
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Professor:"), gbc);
        gbc.gridx = 1;
        professorCombo = new JComboBox<>();
        professorCombo.setRenderer(new ProfessorListRenderer());
        professorCombo.setPreferredSize(new Dimension(300, 30));
        loadProfessors();
        formPanel.add(professorCombo, gbc);

        // Date Selection
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1;
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "MM/dd/yyyy");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner, gbc);

        // Time Selection
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Time:"), gbc);
        gbc.gridx = 1;
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        formPanel.add(timeSpinner, gbc);

        // Duration Selection
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Duration (minutes):"), gbc);
        gbc.gridx = 1;
        SpinnerNumberModel durationModel = new SpinnerNumberModel(30, 15, 120, 15);
        durationSpinner = new JSpinner(durationModel);
        formPanel.add(durationSpinner, gbc);

        // Subject
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Subject:"), gbc);
        gbc.gridx = 1;
        subjectField = new JTextField(20);
        formPanel.add(subjectField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);

        // Wrap the form in a scroll pane
        JScrollPane scrollPaneForm = new JScrollPane(formPanel);
        scrollPaneForm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForm.setBorder(null);
        add(scrollPaneForm, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton bookButton = new JButton("Book Appointment");
        styleButton(bookButton);
        bookButton.addActionListener(e -> handleBooking());
        buttonPanel.add(bookButton);

        add(buttonPanel, BorderLayout.SOUTH);

        professorCombo.addActionListener(e -> {
            Professor selected = (Professor) professorCombo.getSelectedItem();
            if (selected != null) {
                showProfessorDetails(selected);
            }
        });
    }

    private void loadProfessors() {
        DefaultComboBoxModel<Professor> model = new DefaultComboBoxModel<>();
        
        // Get all users and filter professors
        for (User user : userController.getAllUsers()) {
            if (user instanceof Professor) {
                model.addElement((Professor) user);
            }
        }
        
        professorCombo.setModel(model);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(150, 35));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void handleBooking() {
        try {
            Professor selectedProfessor = (Professor) professorCombo.getSelectedItem();
            if (selectedProfessor == null) {
                throw new IllegalStateException("Please select a professor");
            }

            LocalDateTime dateTime = getSelectedDateTime();
            Duration duration = Duration.ofMinutes((int) durationSpinner.getValue());
            String subject = subjectField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (subject.isEmpty()) {
                throw new IllegalStateException("Please enter a subject");
            }

            String appointmentId = appointmentController.createAppointment(
                userController.getCurrentUser().getId(),
                selectedProfessor.getId(),
                dateTime,
                duration,
                subject,
                description
            );

            JOptionPane.showMessageDialog(this,
                "Appointment booked successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            clearForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error booking appointment: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDateTime getSelectedDateTime() {
        java.util.Date date = (java.util.Date) dateSpinner.getValue();
        java.util.Date time = (java.util.Date) timeSpinner.getValue();
        
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .withHour(time.getHours())
            .withMinute(time.getMinutes());

        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot book appointments in the past");
        }

        return dateTime;
    }

    private void clearForm() {
        professorCombo.setSelectedIndex(0);
        dateSpinner.setValue(new java.util.Date());
        timeSpinner.setValue(new java.util.Date());
        durationSpinner.setValue(30);
        subjectField.setText("");
        descriptionArea.setText("");
    }

    // Custom renderer for professor combo box
    private class ProfessorListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                    int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Professor) {
                Professor prof = (Professor) value;
                value = prof.getName(); // Show only name in the combo box
            }
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private void showProfessorDetails(Professor professor) {
        String details = "<html><b>" + professor.getName() + "</b><br>" +
                        "Subjects:<br>" +
                        String.join("<br>", professor.getTeachingSubjects()) +
                        "</html>";
        
        JOptionPane.showMessageDialog(this, details, 
            "Professor Details", JOptionPane.INFORMATION_MESSAGE);
    }
} 