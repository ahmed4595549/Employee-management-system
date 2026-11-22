package gui;

import model.Employee;
import service.DepartmentManager;
import service.EmployeeManager;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JLabel welcomeLabel;
    private JButton addButton;
    private JButton viewButton;
    private JButton departmentsButton;
    private JButton logoutButton;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    private AddEmployeePanel addPanel;
    private ViewEmployeesPanel viewPanel;
    private UpdateEmployeePanel updatePanel;
    private DepartmentPanel departmentPanel;

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;
    public DashboardFrame(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());// north,west,south

        welcomeLabel = new JLabel("Welcome to ems");
        addButton = new JButton("Add Employee");
        viewButton = new JButton("View Employee");
        departmentsButton = new JButton("Departments");
        logoutButton = new JButton("Logout");
        JPanel bar = new JPanel();
        bar.add(welcomeLabel);
        bar.add(addButton);
        bar.add(viewButton);
        bar.add(departmentsButton);
        bar.add(logoutButton);
        add(bar,BorderLayout.NORTH);
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);// this tow to make one panal to apper at time
        // FIX: AddEmployeePanel's constructor takes only (employeeManager, departmentManager) - removed the extra "this"
        addPanel = new AddEmployeePanel(employeeManager, departmentManager);
        viewPanel = new ViewEmployeesPanel(employeeManager, departmentManager, this);
        updatePanel = new UpdateEmployeePanel(employeeManager, departmentManager, this);
        departmentPanel = new DepartmentPanel(departmentManager);
        contentPanel.add(viewPanel, "VIEW");
        contentPanel.add(addPanel, "ADD");
        contentPanel.add(updatePanel, "UPDATE");
        contentPanel.add(departmentPanel, "DEPARTMENTS");
        add(contentPanel, BorderLayout.CENTER);
        addButton.addActionListener(e -> showPanel("ADD"));
        viewButton.addActionListener(e -> showPanel("VIEW"));
        departmentsButton.addActionListener(e -> showPanel("DEPARTMENTS"));
        logoutButton.addActionListener(e -> handleLogout());
        showPanel("VIEW"); // first screen
        setVisible(true);

    }
    private void handleLogout() {
        this.dispose();
        new LoginFrame(employeeManager, departmentManager);
    }

    public void showPanel(String name) {
        switch (name) {
            case "ADD":
                addPanel.refreshDepartments();
                break;
            case "VIEW":
                viewPanel.refreshDepartments();
                viewPanel.refreshTable();
                break;
            case "DEPARTMENTS":
                departmentPanel.refreshTable();
                break;
            default:
                break;
        }
        cardLayout.show(contentPanel, name);
    }


    public void openUpdate(Employee employee) {
        updatePanel.refreshDepartments();
        updatePanel.loadEmployee(employee);
        showPanel("UPDATE");
    }


}