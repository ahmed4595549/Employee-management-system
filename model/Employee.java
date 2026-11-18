package model;

public abstract class Employee {
    private int employeeId;
    private String fullname;
    private String position;
    private  int age;
    Department department;
    private static  int count;

    public Employee(int employeeId, String fullname, String position, int age, Department department) {
        this.employeeId = ++count;
        this.fullname = fullname;
        this.position = position;
        this.age = age;
        this.department = department;
    }
    public int getEmployeeId() {
        return employeeId;
    }
    public int getAge() {
        return age;
    }

    public static int getCount() {
        return count;
    }

    public Department getDepartment() {
        return department;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPosition() {
        return position;
    }
    public abstract double calculateSalary();
    public abstract String getEmployeeType();
    public abstract String toFileLine();


}
