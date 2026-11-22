package model;

public abstract class Employee {
    private int employeeId;
    private String fullname;
    private String position;
    private  int age;
    private   Department department;


    public Employee(int employeeId, String fullname, String position, int age, Department department) {
        this.employeeId = employeeId;
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
