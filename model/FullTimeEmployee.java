package model;

public class FullTimeEmployee extends Employee {

    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int employeeId, String fullname, String position, int age, Department department, double basicSalary, double bonus) {
        super(employeeId, fullname, position, age, department);
        this.basicSalary = basicSalary;
        this.bonus = bonus;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getBonus() {
        return bonus;
    }

    @Override
    public double calculateSalary() {
        return basicSalary + bonus;
    }

    @Override
    public String getEmployeeType() {
        return "FULL";
    }


    @Override
    public String toFileLine() {
        return getEmployeeType()+"|"+getEmployeeId()+"|"+getFullname()+"|"+getAge()+"|"
                +getDepartment().getDepartmentId()+"|"+getPosition()+"|"+basicSalary+"|"+bonus;
    }

}
