package gui;

import model.Department;
import service.DepartmentManager;
import service.InputValidator;
import service.ValidationException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DepartmentPanel extends JPanel {

    private JTextField deptIdField = new JTextField(8);
    private JTextField deptNameField = new JTextField(15);
    private JButton addDeptButton = new JButton("Add Department");
    private JTable departmentTable;
    private DefaultTableModel tableModel;
    //

    private DepartmentManager departmentManager;

    public DepartmentPanel(DepartmentManager departmentManager) {
        this.departmentManager = departmentManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        form.add(new JLabel("Department ID:"));
        form.add(deptIdField);
        form.add(new JLabel("Department Name:"));
        form.add(deptNameField);
        form.add(addDeptButton);
        add(form, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            // cannot edit from the table it will be from the department manger
        };
        departmentTable = new JTable(tableModel);
        add(new JScrollPane(departmentTable), BorderLayout.CENTER);

        addDeptButton.addActionListener(e -> handleAddDepartment());
        refreshTable();
    }
    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Department d : departmentManager.getAllDepartments()) {
            tableModel.addRow(new Object[]{d.getDepartmentId(), d.getDepartmentName()});
        }
    }
    private void handleAddDepartment() {
        try {
            int id = InputValidator.parsePositiveInt(deptIdField.getText(), "Department ID");
            String name = InputValidator.requireNotEmpty(deptNameField.getText(), "Department name");
            departmentManager.addDepartment(new Department(id, name)); // manager checks duplicates
            JOptionPane.showMessageDialog(this, "Department added successfully!");
            deptIdField.setText("");
            deptNameField.setText("");
            refreshTable();
        } catch (ValidationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Invalid input", JOptionPane.ERROR_MESSAGE);

        }
    }

}