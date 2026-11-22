package io;


import model.Department;
import model.Employee;
import model.FullTimeEmployee;
import model.PartTimeEmployee;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeFileHandler {

    private String filePath;

    public EmployeeFileHandler(String filePath) {
        this.filePath = filePath;
    }

    public void saveEmployees(List<Employee> employees) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath))) {
            // a way to deal with the file (use the resourse then close it auto)

            for (Employee e : employees) {
                writer.write(e.toFileLine());
                writer.newLine();
            }
        }
    }
    public List<Employee> loadEmployees(List<Department> departments)
            throws IOException {
        //we need to connect the employee with hte real object

        List<Employee> employees = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            return employees;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Employee e = parseEmployee(line, departments);
                    employees.add(e);

                } catch (IllegalArgumentException ex) {
                    System.out.println("Invalid employee data: " + line);
                }
            }
        }

        return employees;
    }

    private Employee parseEmployee(String line,
                                   List<Department> departments) {

        String[] parts = line.split("\\|", -1);

        if (parts.length != 8) {
            throw new IllegalArgumentException("Invalid data");
        }

        int id = Integer.parseInt(parts[1]);
        String name = parts[2];
        int age = Integer.parseInt(parts[3]);
        int deptId = Integer.parseInt(parts[4]);
        String position = parts[5];

        double first = Double.parseDouble(parts[6]);
        double second = Double.parseDouble(parts[7]);

        Department department = findDepartment(deptId, departments);

        if (parts[0].equals("FULL")) {

            return new FullTimeEmployee(
                    id, name, age, position, department, first, second
            );

        } else if (parts[0].equals("PART")) {

            return new PartTimeEmployee(
                    id, name, age, position, department, first, second
            );
        }

        throw new IllegalArgumentException("Unknown employee type");
    }


    private Department findDepartment(int departmentId,
                                      List<Department> departments) {

        for (Department d : departments) {

            if (d.getDepartmentId() == departmentId) {
                return d;
            }
        }

        throw new IllegalArgumentException("Department not found");
    }
}
