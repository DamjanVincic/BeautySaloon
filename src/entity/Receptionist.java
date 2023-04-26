package entity;

public class Receptionist extends Employee {
    public Receptionist(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(Role.RECEPTIONIST, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
    }
    public Receptionist(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        this(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.setId(id);
    }
}
