package entity;

import java.util.Objects;

public class Service {
    private static int count = 0;

    private TreatmentType treatmentType;
    private String serviceType; // gel...
    private double price;
    private int id;

    public Service(TreatmentType treatmentType, String serviceType, double price) {
        this(0, treatmentType, serviceType, price);
        this.id = ++count;
    }

    public Service(int id, TreatmentType treatmentType, String serviceType, double price) {
        this.treatmentType = treatmentType;
        this.serviceType = serviceType;
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

    public TreatmentType getTreatmentType() {
        return this.treatmentType;
    }
    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
    }

    public String getServiceType() {
        return this.serviceType;
    }
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public static void setCount(int count) {
        Service.count = count;
    }

    @Override
    public boolean equals(Object other) {
        return this.getId() == ((Service)other).getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toFileString() {
        // return this.treatment.toFileString() + "," + this.type;
        return this.id + "," + this.treatmentType.getId() + "," + this.serviceType + "," + this.price;
    }
}
