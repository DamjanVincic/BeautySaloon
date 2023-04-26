package utils;

public class AppSettings {
    private String userFilename, treatmentFilename, treatmentTypeFilename, scheduledTreatmentFilename, priceFilename;
	
	public AppSettings(String userFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) {
		this.userFilename = userFilename;
        this.treatmentFilename = treatmentFilename;
        this.treatmentTypeFilename = treatmentTypeFilename;
        this.scheduledTreatmentFilename = scheduledTreatmentFilename;
        this.priceFilename = priceFilename;
	}

    public String getUserFilename() {
        return this.userFilename;
    }

    public String getTreatmentFilename() {
        return this.treatmentFilename;
    }

    public String getTreatmentTypeFilename() {
        return this.treatmentTypeFilename;
    }

    public String getScheduledTreatmentFilename() {
        return this.scheduledTreatmentFilename;
    }

    public String getPriceFilename() {
        return this.priceFilename;
    }
}
