package model;

public class Department {
    private int departmentId;
    private String departmentName;
    public Department(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }
    public int getDepartmentId() {
        return departmentId;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public String toFileLine(){
        return departmentId+"|"+departmentName;
    }

    // JComboBox<Department> to display the name
    @Override
    public String toString() {
        return departmentName;
    }


}
