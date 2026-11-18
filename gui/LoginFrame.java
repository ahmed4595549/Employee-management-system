package gui;

import service.DepartmentManager;
import service.EmployeeManager;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;

    public LoginFrame(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        setTitle("Login");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 10, 10));

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        this.add(usernameLabel);
        this.add(usernameField);
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(loginButton);
        this.add(exitButton);


        loginButton.addActionListener(e -> handleLogin());
        exitButton.addActionListener(e -> System.exit(0));




        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateCredentials(username, password)) {
            openDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openDashboard() {
        this.dispose();
        new DashboardFrame(employeeManager,departmentManager);
    }

    private boolean validateCredentials(String username, String password) {
        if (username.trim().equals("") || password.trim().equals("")) {
            return false;
        } else if (username.equals("admin") && password.equals("admin123")) {
            return true;
        } else {
            return false;
        }
    }
}
