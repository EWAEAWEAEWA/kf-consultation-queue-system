package view;

import javax.swing.*;
import java.awt.*;
import controller.UserController;
import controller.AppointmentController;

public class LoginView extends JFrame {
    private UserController userController;
    private AppointmentController appointmentController;
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginView(UserController userController, AppointmentController appointmentController) {
        this.userController = userController;
        this.appointmentController = appointmentController;
        setupUI();
    }

    private void setupUI() {
        setTitle("Consultation Queue System - Login");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Username field
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Password field
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Style buttons
        styleButton(loginButton);
        styleButton(registerButton);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add components to main panel
        mainPanel.add(new JLabel("Welcome to Consultation Queue System", SwingConstants.CENTER), BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add button listeners
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegistration());
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (userController.login(username, password)) {
            openDashboard();
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegistration() {
        new RegistrationView(userController, appointmentController).setVisible(true);
        this.setVisible(false);
    }

    private void openDashboard() {
        DashboardView dashboard = new DashboardView(userController, appointmentController);
        dashboard.setVisible(true);
        this.dispose();
    }
} 