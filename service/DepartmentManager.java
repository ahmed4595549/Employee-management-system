package service;

import io.DepartmentFileHandler;
import model.Department;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DepartmentManager {

    private List<Department> departments;
    private DepartmentFileHandler fileHandler;

    public DepartmentManager(DepartmentFileHandler fileHandler) {
        this.fileHandler = fileHandler;
        departments = new ArrayList<>();
    }

    public void loadFromFile() throws ValidationException {
        try {
            departments = fileHandler.loadDepartments();
        } catch (IOException e) {
            throw new ValidationException("Could not read the departments file: " + e.getMessage()
            );
        }
        if (departments.isEmpty()) {
            departments.add(new Department(1, "IT"));
            departments.add(new Department(2, "Human Resources"));
            departments.add(new Department(3, "Finance"));
            departments.add(new Department(4, "Marketing"));
            departments.add(new Department(5, "Sales"));
            save();
        }
    }

    public void addDepartment(Department department) throws ValidationException  {
        if ( department == null ) {
            throw new ValidationException("Department cannot be null");
        }

        if (department.getDepartmentId() <= 0) {
            throw new ValidationException(
                    "Department ID must be a positive integer."
            );
        }
        String name = department.getDepartmentName();

        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Department name is required.");
        }
        if (existsById(department.getDepartmentId())||existsByName(name)) {
            throw new ValidationException("Department ID  or name is " + " already exists."
            );
        }



    }

    public List<Department> getAllDepartments() {
        List<Department> tempdepartment = new ArrayList<>();
        for (Department department : departments) {
            tempdepartment.add(department);

        }
        return tempdepartment;
    }

    public Department findById(int departmentId) {
        List<Department> tempdepartment = getAllDepartments();
        for (Department department : tempdepartment) {
            if (department.getDepartmentId() == departmentId) {
                return department;
            }

        }
        return null;
    }

    public boolean existsById(int departmentId) {
        List<Department> tempdepartment = getAllDepartments();
        for (Department department : tempdepartment) {

            if (department.getDepartmentId() == departmentId) {
                return true;
            }
        }
        return false;
    }

    public boolean existsByName(String name) {
        List<Department> tempdepartment = getAllDepartments();
        for (Department department : tempdepartment) {
            if (department.getDepartmentName().trim().equalsIgnoreCase(name.trim())) {
                return true;
            }
        }
        return false;
    }

    private void save()throws ValidationException {
        try {
            fileHandler.saveDepartments(departments);
        } catch (IOException e) {
            throw new ValidationException("Could not save the departments file: " + e.getMessage()  );
        }
    }
}
