package entity;

public class Employee extends User{
    private String educationLevel;
    private int workExperience, bonus, salary;

    public Employee(String name, String surname, String gender, String phone, String address, String username, String password, String educationLevel, int workExperience, int bonus, int salary) {
        super(name, surname, gender, phone, address, username, password);
        this.educationLevel = educationLevel;
        this.workExperience = workExperience;
        this.bonus = bonus;
        this.salary = salary;
    }

    public String getEducationLevel() {
        return this.educationLevel;
    }
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    public int getWorkExperience() {
        return this.workExperience;
    }
    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public int getBonus() {
        return this.bonus;
    }
    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public int getSalary() {
        return this.salary;
    }
    public void setSalary(int salary) {
        this.salary = salary;
    }
}
