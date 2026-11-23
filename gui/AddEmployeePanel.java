package gui;

import model.Department;
import model.Employee;
// FIX: these imports were missing (FullTimeEmployee, PartTimeEmployee, InputValidator, ValidationException)
import model.FullTimeEmployee;
import model.PartTimeEmployee;
import service.DepartmentManager;
import service.EmployeeManager;
import service.InputValidator;
import service.ValidationException;

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

    // one card for employee type
    private JPanel salaryCards = new JPanel(new CardLayout());
    // same idea of panel and cards

    private JButton saveButton = new JButton("Save");
    private JButton clearButton = new JButton("Clear");

    private EmployeeManager employeeManager;
    private DepartmentManager departmentManager;



    public AddEmployeePanel(EmployeeManager employeeManager, DepartmentManager departmentManager) {
        this.employeeManager = employeeManager;
        this.departmentManager = departmentManager;

        buildLayout();
        refreshDepartments();

        // to get the depatements and put them in the  JComboBox<Department>

        typeComboBox.addActionListener(e -> handleTypeChange());
        // to handle the change in the type and show the salary feilds of each one
        saveButton.addActionListener(e -> handleSave());
        clearButton.addActionListener(e -> clearForm());
        handleTypeChange();// to handle what will appear suit  the employee type

    }


    public void refreshDepartments() {
        departmentComboBox.removeAllItems();
        for(Department D:departmentManager.getAllDepartments()){
            departmentComboBox.addItem(D);
        }
    }
    public boolean isFullTime(){
        return typeComboBox.getSelectedIndex()==0;
    }

    public void handleTypeChange(){
        CardLayout cardLayout = (CardLayout) salaryCards.getLayout();
        cardLayout.show (salaryCards,isFullTime()?"FULL":"PART");
    }
    public void handleSave() {
        try {
            Employee employee = buildEmployee();

            employeeManager.addEmployee(employee);

            JOptionPane.showMessageDialog(this, "Employee added successfully!");
            clearForm();

        }
        catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }
    private Employee buildEmployee() throws ValidationException {

        int id = InputValidator.parsePositiveInt(idField.getText(), "Employee ID");

        String name = InputValidator.requireNotEmpty(nameField.getText(), "Full name");

        int age = InputValidator.parsePositiveInt(ageField.getText(), "Age");

        String position = InputValidator.requireNotEmpty(positionField.getText(), "Position");

        Department department = (Department) departmentComboBox.getSelectedItem();
        if (department == null) {
            throw new ValidationException("select a department.");
        }

        if (isFullTime()) {
            double basic = InputValidator.parseNonNegativeDouble(basicSalaryField.getText(), "Basic salary");
            double bonus = InputValidator.parseNonNegativeDouble(bonusField.getText(), "Bonus");
            return new FullTimeEmployee(id, name, position, age, department, basic, bonus);
        } else {
            double rate = InputValidator.parseNonNegativeDouble(hourlyRateField.getText(), "Hourly rate");
            double hours = InputValidator.parseNonNegativeDouble(hoursWorkedField.getText(), "Hours worked");
            return new PartTimeEmployee(id, name, position, age, department, rate, hours);
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
            // handel if there is no departemnt on it
        }
        handleTypeChange();
    }
    private void buildLayout() {
        setLayout(new BorderLayout());
        // BorderLayout  north,west,south,center
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
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


        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        // sapce between the elenemnts
        c.gridx = 0;
        c.gridy = row++;
        c.gridwidth = 2;
        form.add(salaryCards, c);
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
        // creat a lable

        c.gridx = 1;
        panel.add(field, c);
        // creat a field
    }



}