package io;
import model.Department;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentFileHandler {
    private String filePath;
    public DepartmentFileHandler(String fileName) {
        this.filePath = fileName;

    }

    public void saveDepartments(List<Department> departments)
            throws IOException {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath))) {

            for (Department d : departments) {
                writer.write(d.toFileLine());
                writer.newLine();
            }
        }
    }
    public List<Department> loadDepartments()
            throws IOException {
        List<Department> departments = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return departments;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() == 0) {
                    continue;
                    // this a req from the documnemt
                }
                try {
                    Department d1 = parseDepartment(line);
                    departments.add(d1);
                } catch (Exception E) {
                    System.out.println("Error while parsing file line " + line);
                }
            }
        }
return  departments;

    }
    private Department parseDepartment(String line)
            throws IOException {
        String[] parts = line.split("//|", -1);
        // -1 to keep  the last cell
        if (parts.length != 2) {
            throw new IllegalArgumentException("invald");

        }
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        return new Department(id, name);
    }

    }



}
