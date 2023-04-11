package entity;

public class TreatmentType {
    private Treatment treatment;
    private String type; // gel...

    public TreatmentType(Treatment treatment, String type) {
        this.treatment = treatment;
        this.type = type;
    }

    public Treatment getTreatment() {
        return this.treatment;
    }
    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
