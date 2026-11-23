
package service;

import io.EmployeeFileHandler;
import model.Department;
import model.Employee;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class EmployeeManager {

    private List<Employee> employees;
    private DepartmentManager departmentManager;
    private EmployeeFileHandler fileHandler;

    public EmployeeManager(DepartmentManager departmentManager, EmployeeFileHandler fileHandler) {
        this.departmentManager = departmentManager;
        this.fileHandler = fileHandler;
        employees = new ArrayList<>();
    }

    public void loadFromFile() throws ValidationException {
        try {
            employees = fileHandler.loadEmployees(
                    departmentManager.getAllDepartments()
            );
        } catch (IOException e) {
            employees = new ArrayList<>();
            throw new ValidationException(
                    "Could not read the employees file: " + e.getMessage()
            );
        }
    }

    public void addEmployee(Employee employee) {
        validateEmployee(employee,-1);
        employees.add(employee);
        try {
            save();
        }
        catch  (ValidationException e) {
            employees.remove(employee);
            throw e;
        }


    }

    public void updateEmployee(int currentId, Employee updated) throws ValidationException {
        int index = indexOf(currentId);
        if (index == -1) {
            throw new ValidationException("Employee not found.");
        }
        validateEmployee(updated, currentId);

        Employee old = employees.get(index);
        employees.set(index, updated);
        try {
            save();
        } catch (ValidationException e) {
            employees.set(index, old);
            throw e;
        }
    }
    public void deleteEmployee(int employeeId) {
        int index= indexOf(employeeId);
        if(index == -1){
            throw new ValidationException("Employee does not exist");
        }
        Employee removed = employees.get(index);
        employees.remove(index);
        try {
            save();
        }
        catch (ValidationException e) {
            employees.add(index,removed);
            throw e;
        }
    }


    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);

    }

    public Employee findById(int employeeId) {
        List<Employee> employees = getAllEmployees();
        for (Employee employee : employees) {
            if (employee.getEmployeeId() == employeeId) {
                return employee;
            }
        }
        return null;
    }

    public List<Employee> searchByName(String name) {
        List<Employee> employees = getAllEmployees();
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if(employee.getFullname().trim().toLowerCase().contains(name.trim().toLowerCase()) ){
                result.add(employee);
            }

        }
        return result;
    }

    public List<Employee> filterByDepartment(Department department) {
        List<Employee> employees = getAllEmployees();
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employees) {
            if(employee.getDepartment().getDepartmentId() == department.getDepartmentId()){
                result.add(employee);
            }
        }
        return result;
    }

    public boolean isIdTaken(int id, int excludedId) {
        for (Employee e : employees) {
            if (e.getEmployeeId() == id && e.getEmployeeId() != excludedId) {
                return true;
            }
        }
        return false;
    }

    private void validateEmployee(Employee e, int excludedId) {
        if (e==null) {
            throw new ValidationException("Employee does not exist");
        }
        if (e.getEmployeeId()<=0){
            throw new ValidationException("Employee id is not right ");
        }
        if (isIdTaken(e.getEmployeeId(), excludedId)) {
            throw new ValidationException("Error for a duplicated employee id");
        }
        if( e.getFullname()==null || e.getFullname().trim().isEmpty() ){
            throw new ValidationException("Employee fullname is empty");
        }
        if (e.getPosition()==null || e.getPosition().trim().isEmpty() ){
            throw new ValidationException("Employee position is empty");
        }
        if(e.getFullname().contains("|")||e.getPosition().contains("|")){
            throw new ValidationException("Employee fullname contains invalid characters");

        }
        if (e.getAge()<=0){
            throw new ValidationException("Employee age is wrong");
        }
        if (e.getDepartment()==null||!departmentManager.existsById(e.getDepartment().getDepartmentId())){
            throw new ValidationException("Department does not exist or not unique");
        }
        String type = e.getEmployeeType();

        if (!type.equals("FULL") && !type.equals("PART")) {
            throw new ValidationException("Unsupported employee type.");
        }
        double salary = e.calculateSalary();

        if ( salary < 0) {
            throw new ValidationException(
                    "Salary information must be valid and non-negative."
            );
        }


    }

    private void save() throws ValidationException {
        try {
            fileHandler.saveEmployees(employees);
        } catch (IOException e) {
            throw new ValidationException(
                    "Could not save the employees file: " + e.getMessage()
            );
        }
    }
    private int indexOf(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == id) {
                return i;
            }
        }
        return -1;
    }
}
