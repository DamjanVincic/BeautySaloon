package entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Service {
    private static int count = 0;

    private TreatmentType treatmentType;
    private String serviceType; // gel...
    private double price;
    private LocalTime length;
    private boolean deleted;
    private int id;

    public Service(TreatmentType treatmentType, String serviceType, double price, LocalTime length, boolean deleted) {
        this(0, treatmentType, serviceType, price, length, deleted);
        this.id = ++count;
    }

    public Service(int id, TreatmentType treatmentType, String serviceType, double price, LocalTime length, boolean deleted) {
        this.treatmentType = treatmentType;
        this.serviceType = serviceType;
        this.price = price;
        this.length = length;
        this.deleted = deleted;
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

    public LocalTime getLength() {
        return this.length;
    }

    public void setLength(LocalTime length) {
        this.length = length;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
    public void delete() {
        this.deleted = true;
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

    @Override
    public String toString() {
        return String.format("Treatment type: %s, Service type: %s, Price: %s, Length: %s", this.treatmentType.isDeleted() ? "Deleted" : this.treatmentType, this.serviceType, this.price, this.length.format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public String toFileString() {
        // return this.treatment.toFileString() + "," + this.type;
        return this.id + "," + this.treatmentType.getId() + "," + this.serviceType + "," + this.price + "," + this.length.format(DateTimeFormatter.ofPattern("HH:mm")) + "," + this.deleted;
    }
}
