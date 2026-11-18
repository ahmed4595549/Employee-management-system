package gui;

import model.Department;
import model.Employee;
import service.DepartmentManager;
import service.EmployeeManager;

import javax.swing.*;
import java.awt.*;

public class AddEmployeePanel extends  JPanel {
    private JTextField idField = new JTextField(15);
    private JTextField nameField = new JTextField(15);
    private JTextField ageField = new JTextField(15);
    private JTextField positionField = new JTextField(15);
    private JComboBox<Department> departmentComboBox = new JComboBox<>();
    private JComboBox<String> typeComboBox = new JComboBox<>(new String[]{"Full-Time", "Part-Time"});

    private JTextField basicSalaryField = new JTextField(15);
    private JTextField bonusField = new JTextField(15);
    private JTextField hourlyRateField = new JTextField(15);
    private JTextField hoursWorkedField = new JTextField(15);

    // Salary section: one card per employee type
    private JPanel salaryCards = new JPanel(new CardLayout());

    private JButton saveButton = new JButton("Save");
    private JButton clearButton = new JButton("Clear");

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;

    public AddEmployeePanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        buildLayout();
        refreshDepartments();
        // to get the depatements and put them

        typeComboBox.addActionListener(e -> handleTypeChange());
        saveButton.addActionListener(e -> handleSave());
        clearButton.addActionListener(e -> clearForm());
        handleTypeChange();// to handel what will appear

    }


    public void refreshDepartments() {
        departmentComboBox.removeAllItems();
        for(Department D:departmentManager.getALLDepartments()){
            departmentComboBox.addItem(D);
        }
    }
    public boolean isfulltime(){
        return typeComboBox.getSelectedIndex()==0;
    }
    public void handleTypeChange(){
        CardLayout cardLayout = (CardLayout) salaryCards.getLayout();
        cardLayout.show (salaryCards,isfulltime()?"full-time":"part-time");
    }
    public void handleSave() {
        try {
            Employee employee = buildEmployee();          // 1) parse + build (may throw)
            employeeManager.addEmployee(employee);        // 2) manager: unique ID, save, ...
            JOptionPane.showMessageDialog(this, "Employee added successfully!");

    }    catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }}
    private Employee buildEmployee() throws ValidationException {
        int id = InputValidator.parsePositiveInt(idField.getText(), "Employee ID");
        String name = InputValidator.requireNotEmpty(nameField.getText(), "Full name");
        int age = InputValidator.parsePositiveInt(ageField.getText(), "Age");
        String position = InputValidator.requireNotEmpty(positionField.getText(), "Position");

        Department department = (Department) departmentComboBox.getSelectedItem();
        if (department == null) {
            throw new ValidationException("Please select a department.");
        }

        if (isFullTime()) {
            double basic = InputValidator.parseNonNegativeDouble(basicSalaryField.getText(), "Basic salary");
            double bonus = InputValidator.parseNonNegativeDouble(bonusField.getText(), "Bonus");
            return new FullTimeEmployee(id, name, age, position, department, basic, bonus);
        } else {
            double rate = InputValidator.parseNonNegativeDouble(hourlyRateField.getText(), "Hourly rate");
            double hours = InputValidator.parseNonNegativeDouble(hoursWorkedField.getText(), "Hours worked");
            return new PartTimeEmployee(id, name, age, position, department, rate, hours);
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        positionField.setText("");
        basicSalaryField.setText("");
        bonusField.setText("");
        hourlyRateField.setText("");
        hoursWorkedField.setText("");
        typeComboBox.setSelectedIndex(0);
        if (departmentComboBox.getItemCount() > 0) {
            departmentComboBox.setSelectedIndex(0);
        }
        //
        handleTypeChange();
    }
    private void buildLayout() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        // to make a space from the bounders

        int row =0;
        addRow(form, row++, "Employee ID:", idField);
        addRow(form, row++, "Full Name:", nameField);
        addRow(form, row++, "Age:", ageField);
        addRow(form, row++, "Position:", positionField);
        addRow(form, row++, "Department:", departmentComboBox);
        addRow(form, row++, "Employee Type:", typeComboBox);
        JPanel fullTimePanel =
                new JPanel(new GridLayout(2, 2, 6, 6));
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
        JPanel buttons = new JPanel();

        buttons.add(saveButton);
        buttons.add(clearButton);


    }
    private void addRow(JPanel panel, int row, String label, JComponent field) {

        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(6, 6, 6, 6);
        c.gridy = row;
        // to select which row
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        panel.add(new JLabel(label), c);

        c.gridx = 1;
        panel.add(field, c);
    }



}
