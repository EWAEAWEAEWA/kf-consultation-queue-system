import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import controller.*;
import view.LoginView;
import util.SystemInitializer;

public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start application on EDT
        SwingUtilities.invokeLater(() -> {
            // Create shared controllers
            UserController userController = new UserController();
            AppointmentController appointmentController = new AppointmentController();
            NotificationController notificationController = new NotificationController();

            // Initialize system with sample data
            SystemInitializer initializer = new SystemInitializer(userController, appointmentController, notificationController);
            initializer.initializeSystem();

            // Pass both controllers to LoginView
            LoginView loginView = new LoginView(userController, appointmentController);
            loginView.setVisible(true);

            // Debug output
            System.out.println("Application started");
            userController.getAllProfessors().forEach(prof -> 
                System.out.println("Professor: " + prof.getName() + " - " + prof.getId()));
        });
    }
} 