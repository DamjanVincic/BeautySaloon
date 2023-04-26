package manage;

import utils.AppSettings;

public class ManagerFactory {
    private AppSettings appSettings;
    private UserManager userManager;
    private TreatmentManager treatmentManager;
    private TreatmentTypeManager treatmentTypeManager;
    private ScheduledTreatmentManager scheduledTreatmentManager;
    private PriceListManager priceListManager;

    public ManagerFactory(AppSettings appSettings) {
        this.appSettings = appSettings;

        this.treatmentManager = new TreatmentManager(this.appSettings.getTreatmentFilename());
        this.userManager = new UserManager(this.appSettings.getUserFilename(), treatmentManager);

        this.treatmentTypeManager = new TreatmentTypeManager(this.appSettings.getTreatmentTypeFilename(), this.treatmentManager);
        this.priceListManager = new PriceListManager(this.appSettings.getPriceFilename(), this.treatmentTypeManager);
        // this.treatmentTypeManager.setPriceListManager(priceListManager);
        
        this.scheduledTreatmentManager = new ScheduledTreatmentManager(this.appSettings.getScheduledTreatmentFilename(), this.userManager, this.treatmentTypeManager);
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public TreatmentManager getTreatmentManager() {
        return this.treatmentManager;
    }

    public TreatmentTypeManager getTreatmentTypeManager() {
        return this.treatmentTypeManager;
    }

    public ScheduledTreatmentManager getScheduledTreatmentManager() {
        return this.scheduledTreatmentManager;
    }

    public PriceListManager getPriceListManager() {
        return this.priceListManager;
    }

    public void loadData() {
        this.treatmentManager.loadData();
        this.userManager.loadData();
        this.treatmentTypeManager.loadData();
        this.scheduledTreatmentManager.loadData();
        this.priceListManager.loadData();
    }
}
