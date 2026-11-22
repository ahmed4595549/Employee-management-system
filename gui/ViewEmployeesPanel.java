package gui;

import model.Department;
import model.Employee;
import service.DepartmentManager;
import service.EmployeeManager;
import service.InputValidator;
import service.ValidationException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewEmployeesPanel extends JPanel {

    private static final Department ALL_DEPARTMENTS = new Department(-1, "All Departments");

    private JTable employeeTable;
    private EmployeeTableModel tableModel = new EmployeeTableModel();

    private JTextField searchField = new JTextField(12);
    private JComboBox<String> searchByComboBox = new JComboBox<>(new String[]{"ID", "Name"});
    private JComboBox<Department> departmentFilterComboBox = new JComboBox<>();

    private JButton searchButton = new JButton("Search");
    private JButton resetButton = new JButton("Reset");
    private JButton editButton = new JButton("Edit Selected");
    private JButton deleteButton = new JButton("Delete Selected");

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;
    private DashboardFrame dashboard;

    // true while we fill/reset the filter combo by code, so its listener does not fire
    private boolean updatingCombo = false;

    public ViewEmployeesPanel(EmployeeManager employeeManager, DepartmentManager departmentManager,
                              DashboardFrame dashboard) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;
        this.dashboard = dashboard;

        buildLayout();
        refreshDepartments();
        refreshTable();

        searchButton.addActionListener(e -> handleSearch());
        searchField.addActionListener(e -> handleSearch()); // Enter key
        departmentFilterComboBox.addActionListener(e -> handleFilter());
        resetButton.addActionListener(e -> handleReset());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
    }

    // Show all employees
    public void refreshTable() {
        tableModel.setEmployees(employeeManager.getAllEmployees());
    }

    public void refreshDepartments() {
        updatingCombo = true;
        departmentFilterComboBox.removeAllItems();
        departmentFilterComboBox.addItem(ALL_DEPARTMENTS);
        for (Department d : departmentManager.getAllDepartments()) {
            departmentFilterComboBox.addItem(d);
        }
        departmentFilterComboBox.setSelectedIndex(0);
        updatingCombo = false;
    }

    private void handleSearch() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a value to search for.",
                    "Search", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        setFilterToAll();

        if (searchByComboBox.getSelectedIndex() == 0) { // search by ID
            try {
                int id = InputValidator.parsePositiveInt(text, "Employee ID");
                Employee found = employeeManager.findById(id);
                List<Employee> result = new ArrayList<>();
                if (found != null) {
                    result.add(found);
                }
                showResults(result, "No employee found with ID " + id + ".");
            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        } else { // search by name
            showResults(employeeManager.searchByName(text), "No employee found with that name.");
        }
    }

    private void handleFilter() {
        if (updatingCombo) {
            return;
        }
        Department selected = (Department) departmentFilterComboBox.getSelectedItem();
        searchField.setText("");
        if (selected == null || selected == ALL_DEPARTMENTS) {
            refreshTable();
        } else {
            showResults(employeeManager.filterByDepartment(selected),
                    "No employees in the " + selected.getDepartmentName() + " department.");
        }
    }

    private void handleReset() {
        searchField.setText("");
        setFilterToAll();
        refreshTable();
    }

    private void handleEdit() {
        Employee selected = getSelectedEmployee();
        if (selected == null) {
            showNoSelectionMessage();
            return;
        }
        dashboard.openUpdate(selected);
    }

    private void handleDelete() {
        Employee selected = getSelectedEmployee();
        if (selected == null) {
            showNoSelectionMessage();
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                // FIX: getFullname() (not getFullName())
                "Are you sure you want to delete " + selected.getFullname()
                        + " (ID " + selected.getEmployeeId() + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            employeeManager.deleteEmployee(selected.getEmployeeId());
            JOptionPane.showMessageDialog(this, "Employee deleted successfully!");
            handleReset();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // The real Employee object behind the selected row, or null if nothing is selected
    private Employee getSelectedEmployee() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            return null;
        }
        return tableModel.getEmployeeAt(employeeTable.convertRowIndexToModel(row));
    }

    private void showResults(List<Employee> result, String emptyMessage) {
        tableModel.setEmployees(result);
        if (result.isEmpty()) {
            JOptionPane.showMessageDialog(this, emptyMessage,
                    "No results", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showNoSelectionMessage() {
        JOptionPane.showMessageDialog(this, "Please select an employee from the table first.",
                "No selection", JOptionPane.WARNING_MESSAGE);
    }

    private void setFilterToAll() {
        updatingCombo = true;
        if (departmentFilterComboBox.getItemCount() > 0) {
            departmentFilterComboBox.setSelectedIndex(0);
        }
        updatingCombo = false;
    }

    private void buildLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        top.add(new JLabel("Search by:"));
        top.add(searchByComboBox);
        top.add(searchField);
        top.add(searchButton);
        top.add(new JLabel("Department:"));
        top.add(departmentFilterComboBox);
        top.add(resetButton);
        add(top, BorderLayout.NORTH);

        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeeTable.setRowHeight(24);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottom.add(editButton);
        bottom.add(deleteButton);
        add(bottom, BorderLayout.SOUTH);
    }
}