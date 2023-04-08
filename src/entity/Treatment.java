package entity;

import java.util.ArrayList;

public class Treatment {
    private String treatment; // manikir pedikir
    private ArrayList<Beautician> beauticians;

    public Treatment(String treatment) {
        this.treatment = treatment;
        this.beauticians = new ArrayList<>();
    }

    public String getTreatment() {
        return this.treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public ArrayList<Beautician> getBeauticians() {
        return this.beauticians;
    }
    public void setBeauticians(ArrayList<Beautician> beauticans) {
        this.beauticians = beauticans;
    }

    public void addBeautician(Beautician beautician) {
        this.beauticians.add(beautician);
    }
}
