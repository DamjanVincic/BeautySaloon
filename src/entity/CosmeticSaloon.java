package entity;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import manage.ManagerFactory;
import utils.AppSettings;

public class CosmeticSaloon {
    private static int count = 0;

    private int id;
    private String name;
    private LocalTime startTime;
    private LocalTime endTime;
    private AppSettings appSettings;
    private ManagerFactory managerFactory;

    public CosmeticSaloon(String name, LocalTime startTime, LocalTime endTime, String userFilename, String treatmentTypeFilename, String serviceFilename, String scheduledTreatmentFilename, String priceFilename) {
        this(0, name, startTime, endTime, userFilename, treatmentTypeFilename, serviceFilename, scheduledTreatmentFilename, priceFilename);
        this.id = ++count;
    }

    public CosmeticSaloon(int id, String name, LocalTime startTime, LocalTime endTime, String userFilename, String treatmentTypeFilename, String serviceFilename, String scheduledTreatmentFilename, String priceFilename) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appSettings = new AppSettings(userFilename, treatmentTypeFilename, serviceFilename, scheduledTreatmentFilename, priceFilename);
        this.managerFactory = new ManagerFactory(appSettings, startTime, endTime);
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public AppSettings getAppSettings() {
        return this.appSettings;
    }

    public ManagerFactory getManagerFactory() {
        return this.managerFactory;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public LocalTime getStartTime() {
        return this.startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return this.endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String toFileString() {
        return this.getId() + "," + this.name + "," + this.startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + "," + this.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))  + "," + this.appSettings.getUserFilename() + "," + this.appSettings.getTreatmentTypeFilename() + "," + this.appSettings.getServiceFilename() + "," + this.appSettings.getScheduledTreatmentFilename() + "," + this.appSettings.getPriceFilename();
    }

    public static void setCount(int count) {
        CosmeticSaloon.count = count;
    }
}
