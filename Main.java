import gui.LoginFrame;
import service.*;
import model.*;


public class Main {
    public static void main(String[] args) {
        EmployeeManager em = new EmployeeManager();
        DepartmentManager dm = new DepartmentManager();
        new LoginFrame(em,dm);
        }
    }
