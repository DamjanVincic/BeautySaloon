package manage;

import utils.AppSettings;

public class ManagerFactory {
    private AppSettings appSettings;
    private UserManager userManager;
    private TreatmentTypeManager treatmentTypeManager;
    private ServiceManager serviceManager;
    private ScheduledTreatmentManager scheduledTreatmentManager;
    private PriceListManager priceListManager;

    public ManagerFactory(AppSettings appSettings) {
        this.appSettings = appSettings;

        this.treatmentTypeManager = new TreatmentTypeManager(this.appSettings.getTreatmentTypeFilename());
        this.userManager = new UserManager(this.appSettings.getUserFilename(), treatmentTypeManager);

        this.serviceManager = new ServiceManager(this.appSettings.getServiceFilename(), this.treatmentTypeManager);
        this.priceListManager = new PriceListManager(this.appSettings.getPriceFilename(), this.serviceManager);
        // this.treatmentTypeManager.setPriceListManager(priceListManager);
        
        this.scheduledTreatmentManager = new ScheduledTreatmentManager(this.appSettings.getScheduledTreatmentFilename(), this.userManager, this.serviceManager);
    }

    public UserManager getUserManager() {
        return this.userManager;
    }

    public TreatmentTypeManager getTreatmentTypeManager() {
        return this.treatmentTypeManager;
    }

    public ServiceManager getServiceManager() {
        return this.serviceManager;
    }

    public ScheduledTreatmentManager getScheduledTreatmentManager() {
        return this.scheduledTreatmentManager;
    }

    public PriceListManager getPriceListManager() {
        return this.priceListManager;
    }

    public void loadData() {
        this.treatmentTypeManager.loadData();
        this.userManager.loadData();
        this.serviceManager.loadData();
        this.scheduledTreatmentManager.loadData();
        this.priceListManager.loadData();
    }
}
