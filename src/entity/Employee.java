package entity;

public abstract class Employee extends User {
    private EducationLevel educationLevel;
    private int yearsOfExperience;
    private double bonus, baseSalary;
    private EmployeeRole employeeRole;

    public Employee(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, EmployeeRole employeeRole) {
        super(name, surname, gender, phone, address, username, password);
        this.educationLevel = educationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.bonus = bonus;
        this.baseSalary = baseSalary;
        this.employeeRole = employeeRole;
    }

    public EducationLevel getEducationLevel() {
        return this.educationLevel;
    }
    public void setEducationLevel(EducationLevel educationLevel) {
        this.educationLevel = educationLevel;
    }

    public int getYearsOfExperience() {
        return this.yearsOfExperience;
    }
    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public double getBonus() {
        return this.bonus;
    }
    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getBaseSalary() {
        return this.baseSalary;
    }
    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public EmployeeRole getEmployeeRole() {
        return this.employeeRole;
    }
    public void setEmployeeRole(EmployeeRole employeeRole) {
        this.employeeRole = employeeRole;
    }
}
