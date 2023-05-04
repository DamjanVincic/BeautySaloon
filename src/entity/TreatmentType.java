package entity;

import java.util.Objects;

public class TreatmentType {
    private static int count = 0;

    private Treatment treatment;
    private String type; // gel...
    private double price;
    private int id;

    public TreatmentType(Treatment treatment, String type, double price) {
        this(0, treatment, type, price);
        this.id = ++count;
    }

    public TreatmentType(int id, Treatment treatment, String type, double price) {
        this.treatment = treatment;
        this.type = type;
        this.price = price;
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
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
        return this.id + "," + this.treatment.getId() + "," + this.type + "," + this.price;
    }
}
