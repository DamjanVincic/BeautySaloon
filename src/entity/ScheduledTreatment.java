package entity;

import java.time.LocalDateTime;

public class ScheduledTreatment {
    private static int count = 0;

    private int id;
    private Client client;
    private TreatmentType treatmentType;
    private Beautician beautician;
    private LocalDateTime dateTime;
    private State state;
    private double price;

    public ScheduledTreatment(Client client, TreatmentType treatmentType, Beautician beautician, LocalDateTime dateTime, double price) {
        this.id = ++count;
        this.client = client;
        this.treatmentType = treatmentType;
        this.beautician = beautician;
        this.dateTime = dateTime;
        this.state = State.SCHEDULED;
        this.price = price;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getClient() {
        return this.client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public TreatmentType getTreatmentType() {
        return this.treatmentType;
    }
    public void setTreatmentType(TreatmentType treatmentType) {
        this.treatmentType = treatmentType;
    }

    public Beautician getBeautician() {
        return this.beautician;
    }
    public void setBeautician(Beautician beautician) {
        this.beautician = beautician;
    }

    public LocalDateTime getDateTime() {
        return this.dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
