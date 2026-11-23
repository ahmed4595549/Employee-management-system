package gui;

import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.DepartmentManager;
import service.EmployeeManager;
import service.InputValidator;
import service.ValidationException;

import javax.swing.*;
import java.awt.*;

public class UpdateEmployeePanel extends JPanel {

    private JTextField idField = new JTextField(15);
    private JTextField nameField = new JTextField(15);
    private JTextField ageField = new JTextField(15);
    private JTextField positionField = new JTextField(15);
    private JComboBox<Department> departmentComboBox = new JComboBox<>();
    private JLabel typeLabel = new JLabel("-"); // the type is shown but cannot be changed

    private JTextField basicSalaryField = new JTextField(15);
    private JTextField bonusField = new JTextField(15);
    private JTextField hourlyRateField = new JTextField(15);
    private JTextField hoursWorkedField = new JTextField(15);

    private JPanel salaryCards = new JPanel(new CardLayout());

    private JButton updateButton = new JButton("Update");
    private JButton cancelButton = new JButton("Cancel");


    private Employee selectedEmployee;

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;
    private DashboardFrame dashboard; // to go back to the View screen

    public UpdateEmployeePanel(EmployeeManager employeeManager, DepartmentManager departmentManager,
                               DashboardFrame dashboard) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;
        this.dashboard = dashboard; // only stored here, never used inside the constructor

        buildLayout();
        refreshDepartments();

        updateButton.addActionListener(e -> handleUpdate());
        cancelButton.addActionListener(e -> handleCancel());
    }

    public void refreshDepartments() {
        Department current = (Department) departmentComboBox.getSelectedItem();
        departmentComboBox.removeAllItems();
        for (Department d : departmentManager.getAllDepartments()) {
            departmentComboBox.addItem(d);
        }
        if (current != null) {
            selectDepartment(current);
        }
    }

    public void loadEmployee(Employee employee) {
        selectedEmployee = employee;

        idField.setText(String.valueOf(employee.getEmployeeId()));
        // FIX: getFullname() (not getFullName())
        nameField.setText(employee.getFullname());
        ageField.setText(String.valueOf(employee.getAge()));
        positionField.setText(employee.getPosition());
        selectDepartment(employee.getDepartment());

        CardLayout cards = (CardLayout) salaryCards.getLayout();
        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee full = (FullTimeEmployee) employee;
            typeLabel.setText("Full-Time");
            basicSalaryField.setText(String.valueOf(full.getBasicSalary()));
            bonusField.setText(String.valueOf(full.getBonus()));
            cards.show(salaryCards, "FULL");
        } else if (employee instanceof PartTimeEmployee) {
            PartTimeEmployee part = (PartTimeEmployee) employee;
            typeLabel.setText("Part-Time");
            // FIX: getHourlyRate() now exists in PartTimeEmployee (was getHoursRate)
            hourlyRateField.setText(String.valueOf(part.getHoursRate()));
            hoursWorkedField.setText(String.valueOf(part.getHoursWorked()));
            cards.show(salaryCards, "PART");
        }
    }

    private void handleUpdate() {
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this,
                    "No employee selected. Please choose one from the View Employees screen.",
                    "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int oldId = selectedEmployee.getEmployeeId();   // الـ ID القديم (من الـ object مش من الخانة)
            Employee updated = buildUpdatedEmployee();       // الـ ID الجديد بيتقرا هنا من الخانة
            employeeManager.updateEmployee(oldId, updated);  // نبعت القديم أولاً ثم الجديد
            JOptionPane.showMessageDialog(this, "Employee updated successfully!");
            selectedEmployee = null;
            dashboard.showPanel("VIEW");
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void handleCancel() {
        selectedEmployee = null;
        dashboard.showPanel("VIEW");
    }

    // Builds a new employee of the SAME type as the selected one
    private Employee buildUpdatedEmployee() throws ValidationException {
        int id = InputValidator.parsePositiveInt(idField.getText(), "Employee ID");
        String name = InputValidator.requireNotEmpty(nameField.getText(), "Full name");
        int age = InputValidator.parsePositiveInt(ageField.getText(), "Age");
        String position = InputValidator.requireNotEmpty(positionField.getText(), "Position");

        Department department = (Department) departmentComboBox.getSelectedItem();
        if (department == null) {
            throw new ValidationException("Please select a department.");
        }

        if (selectedEmployee instanceof FullTimeEmployee) {
            double basic = InputValidator.parseNonNegativeDouble(basicSalaryField.getText(), "Basic salary");
            double bonus = InputValidator.parseNonNegativeDouble(bonusField.getText(), "Bonus");
            // FIX: constructor order is (id, name, position, age, department, ...)
            return new FullTimeEmployee(id, name, position, age, department, basic, bonus);
        } else if (selectedEmployee instanceof PartTimeEmployee) {
            double rate = InputValidator.parseNonNegativeDouble(hourlyRateField.getText(), "Hourly rate");
            double hours = InputValidator.parseNonNegativeDouble(hoursWorkedField.getText(), "Hours worked");
            // FIX: same argument-order fix
            return new PartTimeEmployee(id, name, position, age, department, rate, hours);
        }
        throw new ValidationException("Unsupported employee type.");
    }

    private void selectDepartment(Department department) {
        for (int i = 0; i < departmentComboBox.getItemCount(); i++) {
            if (departmentComboBox.getItemAt(i).getDepartmentId() == department.getDepartmentId()) {
                departmentComboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private void buildLayout() {
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int row = 0;
        addRow(form, row++, "Employee ID:", idField);
        addRow(form, row++, "Full Name:", nameField);
        addRow(form, row++, "Age:", ageField);
        addRow(form, row++, "Position:", positionField);
        addRow(form, row++, "Department:", departmentComboBox);
        addRow(form, row++, "Employee Type:", typeLabel);

        JPanel fullTimePanel = new JPanel(new GridLayout(2, 2, 6, 6));
        fullTimePanel.add(new JLabel("Basic Salary:"));
        fullTimePanel.add(basicSalaryField);
        fullTimePanel.add(new JLabel("Bonus:"));
        fullTimePanel.add(bonusField);

        JPanel partTimePanel = new JPanel(new GridLayout(2, 2, 6, 6));
        partTimePanel.add(new JLabel("Hourly Rate:"));
        partTimePanel.add(hourlyRateField);
        partTimePanel.add(new JLabel("Hours Worked:"));
        partTimePanel.add(hoursWorkedField);

        salaryCards.add(fullTimePanel, "FULL");
        salaryCards.add(partTimePanel, "PART");

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridx = 0;
        c.gridy = row++;
        c.gridwidth = 2;
        form.add(salaryCards, c);

        JPanel buttons = new JPanel();
        buttons.add(updateButton);
        buttons.add(cancelButton);
        c.gridy = row;
        form.add(buttons, c);

        add(form, BorderLayout.NORTH);
    }

    private void addRow(JPanel panel, int row, String label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        panel.add(field, c);
    }
}