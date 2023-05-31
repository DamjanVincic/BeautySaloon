package entity;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Beautician extends Employee {
    private HashMap<Integer, TreatmentType> treatmentTypesTrainedFor;

    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) {
        super(Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentTypesTrainedFor = new HashMap<>();
    }
    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, HashMap<Integer, TreatmentType> treatmentTypesTrainedFor) {
        super(Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentTypesTrainedFor = treatmentTypesTrainedFor;
    }

    public Beautician(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, HashMap<Integer, TreatmentType> treatmentTypesTrainedFor) {
        super(id, Role.BEAUTICIAN, name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
        this.treatmentTypesTrainedFor = treatmentTypesTrainedFor;
    }

    public HashMap<Integer, TreatmentType> getTreatmentTypesTrainedFor() {
        return this.treatmentTypesTrainedFor;
    }
    public void setTreatmentTypesTrainedFor(HashMap<Integer, TreatmentType> treatmentsTrainedFor) {
        this.treatmentTypesTrainedFor = treatmentsTrainedFor;
    } 


    public void addTreatmentType(TreatmentType treatmentType) {
        this.treatmentTypesTrainedFor.put(treatmentType.getId(), treatmentType);
    }

    public void removeTreatmentType(TreatmentType treatmentType) {
        this.treatmentTypesTrainedFor.remove(treatmentType.getId());
    }

    @Override
    public String toString() {
        String treatmentsTrainedForString = this.treatmentTypesTrainedFor.values().stream().map(e -> e.getType()).collect(Collectors.joining(", "));
        return String.format("%s, Treatments trained for: %s", super.toString(), treatmentsTrainedForString);
    }

    public String toFileString() {
        String treatmentsTrainedForString = this.treatmentTypesTrainedFor.keySet().stream().map(String::valueOf).collect(Collectors.joining(";"));
        return super.toFileString() + "," + treatmentsTrainedForString;
    }
}
