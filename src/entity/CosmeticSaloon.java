package entity;

import manage.ManagerFactory;
import utils.AppSettings;

public class CosmeticSaloon {
    private static int count = 0;

    private int id;
    private AppSettings appSettings;
    private ManagerFactory managerFactory;

    public CosmeticSaloon(String userFilename, String treatmentTypeFilename, String serviceFilename, String scheduledTreatmentFilename, String priceFilename) {
        this(0, userFilename, treatmentTypeFilename, serviceFilename, scheduledTreatmentFilename, priceFilename);
        this.id = ++count;
    }

    public CosmeticSaloon(int id, String userFilename, String treatmentTypeFilename, String serviceFilename, String scheduledTreatmentFilename, String priceFilename) {
        this.appSettings = new AppSettings(userFilename, treatmentTypeFilename, serviceFilename, scheduledTreatmentFilename, priceFilename);
        this.managerFactory = new ManagerFactory(appSettings);
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

    public String toFileString() {
        return this.getId() + "," + this.appSettings.getUserFilename() + "," + this.appSettings.getTreatmentTypeFilename() + "," + this.appSettings.getServiceFilename() + "," + this.appSettings.getScheduledTreatmentFilename() + "," + this.appSettings.getPriceFilename();
    }

    public static void setCount(int count) {
        CosmeticSaloon.count = count;
    }
}
