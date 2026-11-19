package gui;

import model.Employee;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EmployeeTableModel extends AbstractTableModel {

    private List<Employee> employees = new ArrayList<>();
    private String[] columnNames = {"ID", "Name", "Age", "Department", "Position", "Type", "Salary"};

    public void setEmployees(List<Employee> employees) {
        this.employees = new ArrayList<>(employees);
        fireTableDataChanged();
    }

    public Employee getEmployeeAt(int row) {
        return employees.get(row);
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Employee e = employees.get(row);
        switch (column) {
            case 0:
                return e.getEmployeeId();
            case 1:
                return e.getFullName();
            case 2:
                return e.getAge();
            case 3:
                return e.getDepartment().getDepartmentName();
            case 4:
                return e.getPosition();
            case 5:
                return e.getEmployeeType().equals("FULL") ? "Full-Time" : "Part-Time";
            case 6:
                // Polymorphism: each employee type calculates its own salary
                return String.format(Locale.US, "%.2f", e.calculateSalary());
            default:
                return "";
        }
    }
}