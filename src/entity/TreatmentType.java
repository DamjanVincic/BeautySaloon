package entity;

import java.util.Objects;

public class TreatmentType {
    private static int count = 0;

    private Treatment treatment;
    private String type; // gel...
    private int id;

    public TreatmentType(Treatment treatment, String type) {
        this.treatment = treatment;
        this.type = type;
        this.id = ++count;
    }

    public TreatmentType(int id, Treatment treatment, String type) {
        this.treatment = treatment;
        this.type = type;
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static void setCount(int count) {
        TreatmentType.count = count;
    }

    @Override
    public boolean equals(Object other) {
        return this.getId() == ((TreatmentType)other).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toFileString() {
        // return this.treatment.toFileString() + "," + this.type;
        return this.id + "," + this.treatment.getId() + "," + this.type;
    }
}
