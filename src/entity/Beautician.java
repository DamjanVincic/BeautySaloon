package entity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Beautician extends Employee {
    private ArrayList<Treatment> treatmentsTrainedFor;

    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentsTrainedFor = new ArrayList<>();
    }

    public Beautician(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(id, Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentsTrainedFor = new ArrayList<>();
    }

    public ArrayList<Treatment> getTreatmentsTrainedFor() {
        return this.treatmentsTrainedFor;
    }
    public void setTreatmentsTrainedFor(ArrayList<Treatment> treatmentsTrainedFor) {
        this.treatmentsTrainedFor = treatmentsTrainedFor;
    }
    

    public void addTreatment(Treatment treatment) {
        this.treatmentsTrainedFor.add(treatment);
    }

    public void removeTreatment(Treatment treatment) {
        this.treatmentsTrainedFor.remove(treatment);
    }

    public String toFileString() {
        String treatmentsTrainedForString = this.treatmentsTrainedFor.stream().map(e -> Integer.toString(e.getId())).collect(Collectors.joining(";"));
        return super.toFileString() + "," + treatmentsTrainedForString;
    }
}
