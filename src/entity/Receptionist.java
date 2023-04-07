package entity;

public class Receptionist extends Employee {
    public Receptionist(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary, EmployeeRole.RECEPTIONIST);
    }
}
