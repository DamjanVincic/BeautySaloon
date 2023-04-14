package manage;

import utils.AppSettings;

public class ManagerFactory {
    private AppSettings appSettings;
    private ClientManager clientManager;
    private EmployeeManager employeeManager;
    private TreatmentManager treatmentManager;
    private TreatmentTypeManager treatmentTypeManager;
    private ScheduledTreatmentManager scheduledTreatmentManager;
    private PriceListManager priceListManager;

    public ManagerFactory(AppSettings appSettings) {
        this.appSettings = appSettings;

        this.clientManager = new ClientManager(this.appSettings.getClientFilename());
        this.employeeManager = new EmployeeManager(this.appSettings.getEmployeeFilename());
        this.clientManager.setEmployeeManager(employeeManager);
        this.employeeManager.setClientManager(clientManager);

        this.treatmentManager = new TreatmentManager(this.appSettings.getTreatmentFilename());
        
        this.treatmentTypeManager = new TreatmentTypeManager(this.appSettings.getTreatmentTypeFilename(), this.treatmentManager);
        this.priceListManager = new PriceListManager(this.appSettings.getPriceFilename(), this.treatmentTypeManager);
        this.treatmentTypeManager.setPriceListManager(priceListManager);
        
        this.scheduledTreatmentManager = new ScheduledTreatmentManager(this.appSettings.getScheduledTreatmentFilename(), this.clientManager, this.treatmentTypeManager, this.employeeManager);
    }

    public ClientManager getClientManager() {
        return this.clientManager;
    }

    public EmployeeManager getEmployeeManager() {
        return this.employeeManager;
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
        this.clientManager.loadData();
        this.employeeManager.loadData();
        this.treatmentManager.loadData();
        this.treatmentManager.loadData();
        this.treatmentTypeManager.loadData();
        this.scheduledTreatmentManager.loadData();
        this.priceListManager.loadData();
    }
}
