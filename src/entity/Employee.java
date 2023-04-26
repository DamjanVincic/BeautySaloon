package entity;

public abstract class Employee extends User {
    private EducationLevel educationLevel;
    private int yearsOfExperience;
    private double bonus, baseSalary;

    public Employee(Role role, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(role, name, surname, gender, phone, address, username, password);
        this.educationLevel = educationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.bonus = bonus;
        this.baseSalary = baseSalary;
    }

    public Employee(int id, Role role, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(id, role, name, surname, gender, phone, address, username, password);
        this.educationLevel = educationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.bonus = bonus;
        this.baseSalary = baseSalary;
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


    public String toFileString() {
        return super.toFileString() + "," + this.getEducationLevel() + "," + this.getYearsOfExperience() + "," + this.getBonus() + "," + this.getBaseSalary();
    }

    public double calculatePaycheck() {
        return this.baseSalary * (1 + this.yearsOfExperience*0.05 + educationLevel.getBonus());
    }
}
