package entity;

public class Treatment {
    private String treatment; // manikir pedikir

    public Treatment(String treatment) {
        this.treatment = treatment;
    }

    public String getTreatment() {
        return this.treatment;
    }
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }


    public String toFileString() {
        // String beautucianString = this.getBeauticians().stream().map(Beautician::getUsername).collect(Collectors.joining(";"));
        return this.treatment; // + beautucianString;
    }
}
