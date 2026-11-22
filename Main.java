import gui.LoginFrame;
import io.DepartmentFileHandler;
import io.EmployeeFileHandler;
import service.DepartmentManager;
import service.EmployeeManager;
import service.ValidationException;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DepartmentFileHandler departmentFileHandler = new DepartmentFileHandler("data/departments.txt");
            EmployeeFileHandler employeeFileHandler = new EmployeeFileHandler("data/employees.txt");

            DepartmentManager departmentManager = new DepartmentManager(departmentFileHandler);
            EmployeeManager employeeManager = new EmployeeManager(departmentManager, employeeFileHandler);

            try {
                departmentManager.loadFromFile();
            } catch (ValidationException e) {
                showLoadError(e.getMessage());
            }
            try {
                employeeManager.loadFromFile();
            } catch (ValidationException e) {
                showLoadError(e.getMessage());
            }

            new LoginFrame(employeeManager, departmentManager);
        });
    }

    // A file problem must not crash the program: tell the user and continue with what we have
    private static void showLoadError(String message) {
        JOptionPane.showMessageDialog(null, message + "\nThe application will start with empty data.",
                "File error", JOptionPane.WARNING_MESSAGE);
    }
}
