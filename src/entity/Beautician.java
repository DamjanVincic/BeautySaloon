package entity;

import java.util.ArrayList;

public class Beautician extends Employee {
    private ArrayList<TreatmentType> treatmentTypesTrainedFor;

    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int workExperience, int bonus, int baseSalary) {
        super(name, surname, gender, phone, address, username, password, educationLevel, workExperience, bonus, baseSalary, EmployeeRole.BEAUTICIAN);
        this.treatmentTypesTrainedFor = new ArrayList<>();
    }

    public ArrayList<TreatmentType> getTreatmentsTrainedFor() {
        return this.treatmentTypesTrainedFor;
    }
    public void setTreatmentsTrainedFor(ArrayList<TreatmentType> treatmentTypesTrainedFor) {
        this.treatmentTypesTrainedFor = treatmentTypesTrainedFor;
    }
}
