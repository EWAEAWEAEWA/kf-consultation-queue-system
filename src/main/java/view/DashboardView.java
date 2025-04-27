package view;

import javax.swing.*;
import java.awt.*;
import controller.*;
import model.*;

public class DashboardView extends JFrame {
    private UserController userController;
    private AppointmentController appointmentController;
    private NotificationController notificationController;
    private JPanel mainContent;
    private NotificationPanel notificationPanel;

    public DashboardView(UserController userController, AppointmentController appointmentController) {
        this.userController = userController;
        this.appointmentController = appointmentController;
        this.notificationController = new NotificationController();
        setupUI();
    }

    private void setupUI() {
        setTitle("Consultation Queue System - Dashboard");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main layout
        setLayout(new BorderLayout());

        // Create sidebar
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        // Create main content panel
        mainContent = new JPanel(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);

        // Create notification panel
        notificationPanel = new NotificationPanel(notificationController, userController);
        add(notificationPanel, BorderLayout.EAST);

        // Load appropriate dashboard based on user role
        loadDashboardContent();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(51, 51, 51));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        User currentUser = userController.getCurrentUser();
        
        // Add user info
        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(nameLabel);
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        // Add menu buttons based on user role
        if (currentUser instanceof Student) {
            addMenuButton(sidebar, "Book Appointment", () -> showBookAppointment());
            addMenuButton(sidebar, "My Appointments", () -> showMyAppointments());
        } else if (currentUser instanceof Professor) {
            addMenuButton(sidebar, "Queue Status", () -> showQueueStatus());
            addMenuButton(sidebar, "Manage Appointments", () -> showManageAppointments());
        } else if (currentUser instanceof Counselor) {
            addMenuButton(sidebar, "Queue Status", () -> showQueueStatus());
            addMenuButton(sidebar, "Manage Appointments", () -> showManageAppointments());
        }

        addMenuButton(sidebar, "Notifications", () -> toggleNotificationPanel());
        
        // Add logout button at bottom
        sidebar.add(Box.createVerticalGlue());
        addMenuButton(sidebar, "Logout", () -> handleLogout());

        return sidebar;
    }

    private void addMenuButton(JPanel sidebar, String text, Runnable action) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 35));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        styleMenuButton(button);
        button.addActionListener(e -> action.run());
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void styleMenuButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void loadDashboardContent() {
        mainContent.removeAll();
        User currentUser = userController.getCurrentUser();
        
        if (currentUser instanceof Student) {
            showBookAppointment();
        } else if (currentUser instanceof Professor || currentUser instanceof Counselor) {
            showQueueStatus();
        }
        
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showBookAppointment() {
        mainContent.removeAll();
        mainContent.add(new AppointmentBookingView(appointmentController, userController), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showMyAppointments() {
        mainContent.removeAll();
        String userId = userController.getCurrentUser().getId();
        JPanel appointmentsPanel = new AppointmentListView(
            appointmentController.getStudentAppointments(userId),
            userController
        );
        mainContent.add(appointmentsPanel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showQueueStatus() {
        mainContent.removeAll();
        mainContent.add(new QueueStatusView(appointmentController, userController), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void showManageAppointments() {
        mainContent.removeAll();
        String userId = userController.getCurrentUser().getId();
        JPanel managePanel = new ManageAppointmentsView(
            appointmentController.getProfessorAppointments(userId),
            appointmentController,
            userController
        );
        mainContent.add(managePanel, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    private void toggleNotificationPanel() {
        notificationPanel.setVisible(!notificationPanel.isVisible());
        revalidate();
        repaint();
    }

    private void handleLogout() {
        userController.logout();
        new LoginView(userController, appointmentController).setVisible(true);
        this.dispose();
    }
} 