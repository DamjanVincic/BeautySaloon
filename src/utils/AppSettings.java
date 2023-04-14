package utils;

public class AppSettings {
    private String clientFilename, employeeFilename, treatmentFilename, treatmentTypeFilename, scheduledTreatmentFilename, priceFilename;
	
	public AppSettings(String clientFilename, String employeeFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) {
		this.clientFilename = clientFilename;
        this.employeeFilename = employeeFilename;
        this.treatmentFilename = treatmentFilename;
        this.treatmentTypeFilename = treatmentTypeFilename;
        this.scheduledTreatmentFilename = scheduledTreatmentFilename;
        this.priceFilename = priceFilename;
	}

    public String getClientFilename() {
        return this.clientFilename;
    }
    
    public String getEmployeeFilename() {
        return this.employeeFilename;
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
