package entity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Beautician extends Employee {
    private ArrayList<TreatmentType> treatmentTypesTrainedFor;

    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentTypesTrainedFor = new ArrayList<>();
    }

    public Beautician(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(id, Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentTypesTrainedFor = new ArrayList<>();
    }

    public ArrayList<TreatmentType> getTreatmentsTrainedFor() {
        return this.treatmentTypesTrainedFor;
    }
    public void setTreatmentsTrainedFor(ArrayList<TreatmentType> treatmentsTrainedFor) {
        this.treatmentTypesTrainedFor = treatmentsTrainedFor;
    }
    

    public void addTreatment(TreatmentType treatment) {
        this.treatmentTypesTrainedFor.add(treatment);
    }

    public void removeTreatment(TreatmentType treatment) {
        this.treatmentTypesTrainedFor.remove(treatment);
    }

    public String toFileString() {
        String treatmentsTrainedForString = this.treatmentTypesTrainedFor.stream().map(e -> Integer.toString(e.getId())).collect(Collectors.joining(";"));
        return super.toFileString() + "," + treatmentsTrainedForString;
    }
}
