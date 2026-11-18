package model;

public class PartTimeEmployee extends Employee {
    private double hoursRate;
    private double hoursWorked;
    public PartTimeEmployee(int employeeId, String fullname, String position, int age, Department department, double hoursRate, double hoursWorked){
        super(employeeId, fullname, position, age, department);
        this.hoursRate = hoursRate;
        this.hoursWorked = hoursWorked;
    }
    public double getHoursRate() {
        return hoursRate;
    }
    public double getHoursWorked() {
        return hoursWorked;
    }
    @Override
    public double calculateSalary() {
        return this.hoursWorked * this.hoursRate;
    }

    @Override
    public String getEmployeeType() {
        return "PART";
    }

    @Override
    public String toFileLine() {
        return  super.getFullname()+"|"+super.getEmployeeId()+"|"+super.getAge()+"|"
                +super.getDepartment()+"|"+this.getHoursRate()+"|"+this.getHoursWorked();
    }

}
