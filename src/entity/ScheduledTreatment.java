package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScheduledTreatment {
    private static int count = 0;

    private int id;
    private Client client;
    private Service service;
    private Beautician beautician;
    private LocalDateTime dateTime;
    private State state;
    private double price;

    public ScheduledTreatment(Client client, Service service, Beautician beautician, LocalDateTime dateTime) {
        /* this.id = ++count;
        this.client = client;
        this.treatmentType = treatmentType;
        this.beautician = beautician;
        this.dateTime = dateTime;
        this.state = State.SCHEDULED;
        this.price = price; */
        this(0, client, service, beautician, dateTime, State.SCHEDULED, null);
        this.id = ++count;
        this.price = client.hasLoyaltyCard() ? price * 0.9 : price;
    }

    public ScheduledTreatment(int id, Client client, Service service, Beautician beautician, LocalDateTime dateTime, State state, Double price) {
        this.id = id;
        this.client = client;
        this.service = service;
        this.beautician = beautician;
        this.dateTime = dateTime;
        this.state = state;
        this.price = price == null ? service.getPrice() : price;
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

    public Service getService() {
        return this.service;
    }
    public void setService(Service service) {
        this.service = service;
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

    public static void setCount(int count) {
        ScheduledTreatment.count = count;
    }

    @Override
    public String toString() {
        return String.format("Client: %s %s, Service: %s, Beautician: %s %s, Type: %s, DateTime: %s, Price: %s", this.client.getName(), this.client.getSurname(), this.service.getServiceType(), this.beautician.getName(), this.beautician.getSurname(), this.service.getTreatmentType().getType(), this.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")), this.price);
    }

    public String toFileString() {
        return this.id + "," + this.client.getId() + "," + this.service.getId() + "," + this.beautician.getId() + "," + this.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH")) + "," + this.state + "," + this.price;
    }
}
