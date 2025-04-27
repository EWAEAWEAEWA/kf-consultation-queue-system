package view;

import javax.swing.*;
import java.awt.*;
import controller.UserController;
import controller.AppointmentController;

public class RegistrationView extends JFrame {
    private UserController userController;
    private AppointmentController appointmentController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField nameField;
    private JTextField emailField;
    private JComboBox<String> userTypeCombo;

    public RegistrationView(UserController userController, AppointmentController appointmentController) {
        this.userController = userController;
        this.appointmentController = appointmentController;
        setupUI();
    }

    private void setupUI() {
        setTitle("Registration");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add form fields
        addFormField(formPanel, "Name:", nameField = new JTextField(20), gbc, 0);
        addFormField(formPanel, "Email:", emailField = new JTextField(20), gbc, 1);
        addFormField(formPanel, "Username:", usernameField = new JTextField(20), gbc, 2);
        addFormField(formPanel, "Password:", passwordField = new JPasswordField(20), gbc, 3);

        // User type combo box
        String[] userTypes = {"Student", "Professor", "Counselor"};
        userTypeCombo = new JComboBox<>(userTypes);
        addFormField(formPanel, "User Type:", userTypeCombo, gbc, 4);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        styleButton(registerButton);
        styleButton(backButton);

        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Add components to main panel
        mainPanel.add(new JLabel("Registration Form", SwingConstants.CENTER), BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add button listeners
        registerButton.addActionListener(e -> handleRegistration());
        backButton.addActionListener(e -> backToLogin());
    }

    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(120, 30));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void handleRegistration() {
        String name = nameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        // Validate fields
        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "All fields are required",
                "Registration Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Register user based on type
        String userId;
        switch (userType) {
            case "Student":
                userId = userController.registerStudent(username, password, name, email);
                break;
            case "Professor":
                userId = userController.registerProfessor(username, password, name, email);
                break;
            case "Counselor":
                userId = userController.registerCounselor(username, password, name, email);
                break;
            default:
                throw new IllegalStateException("Unexpected user type: " + userType);
        }

        if (userId != null) {
            JOptionPane.showMessageDialog(this,
                "Registration successful! Please login.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        }
    }

    private void backToLogin() {
        new LoginView(userController, appointmentController).setVisible(true);
        this.dispose();
    }
} 