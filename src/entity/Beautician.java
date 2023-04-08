package entity;

import java.util.ArrayList;

public class Beautician extends Employee {
    private ArrayList<Treatment> treatmentsTrainedFor;

    public Beautician(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int workExperience, int bonus, int baseSalary) {
        super(name, surname, gender, phone, address, username, password, educationLevel, workExperience, bonus, baseSalary, EmployeeRole.BEAUTICIAN);
        this.treatmentsTrainedFor = new ArrayList<>();
    }

    public ArrayList<Treatment> getTreatmentsTrainedFor() {
        return this.treatmentsTrainedFor;
    }
    public void setTreatmentsTrainedFor(ArrayList<Treatment> treatmentsTrainedFor) {
        this.treatmentsTrainedFor = treatmentsTrainedFor;
    }
}
