package entity;

public class Manager extends Employee {
    public Manager(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(Role.MANAGER, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
    }
    public Manager(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        this(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.setId(id);
    }
}
