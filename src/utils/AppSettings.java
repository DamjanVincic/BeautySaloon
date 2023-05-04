package utils;

public class AppSettings {
    private String userFilename, treatmentTypeFilename, serviceFilename, scheduledTreatmentFilename, priceFilename;
	
	public AppSettings(String userFilename, String treatmentTypeFilename, String serviceFilename, String scheduledTreatmentFilename, String priceFilename) {
		this.userFilename = userFilename;
        this.treatmentTypeFilename = treatmentTypeFilename;
        this.serviceFilename = serviceFilename;
        this.scheduledTreatmentFilename = scheduledTreatmentFilename;
        this.priceFilename = priceFilename;
	}

    public String getUserFilename() {
        return this.userFilename;
    }

    public String getTreatmentTypeFilename() {
        return this.treatmentTypeFilename;
    }

    public String getServiceFilename() {
        return this.serviceFilename;
    }

    public String getScheduledTreatmentFilename() {
        return this.scheduledTreatmentFilename;
    }

    public String getPriceFilename() {
        return this.priceFilename;
    }
}
