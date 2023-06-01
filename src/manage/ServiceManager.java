package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.stream.Collectors;

import entity.TreatmentType;
import entity.ScheduledTreatment;
import entity.Service;

public class ServiceManager {
    private String serviceFile;
    private TreatmentTypeManager treatmentTypeManager;
    private ScheduledTreatmentManager scheduledTreatmentManager;
    private HashMap<Integer, Service> services;

    public ServiceManager(String serviceFile, TreatmentTypeManager treatmentTypeManager) {
        this.serviceFile = serviceFile;
        this.treatmentTypeManager = treatmentTypeManager;
        this.services = new HashMap<>();
    }
    
    public void setScheduledTreatmentManager(ScheduledTreatmentManager scheduledTreatmentManager) {
    	this.scheduledTreatmentManager = scheduledTreatmentManager;
    }

    public HashMap<Integer, Service> getServices() {
        // return this.services;
        return (HashMap<Integer, Service>) this.services.entrySet().stream()
                                        .filter(e -> !e.getValue().isDeleted())
                                        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    public Service findServiceByID(int id) {
        return this.services.get(id);
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.serviceFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Service service = new Service(Integer.parseInt(data[0]), treatmentTypeManager.findTreatmentTypeByID(Integer.parseInt(data[1])), data[2], Double.parseDouble(data[3]), Integer.parseInt(data[4]), Boolean.parseBoolean(data[5]));
				this.services.put(service.getId(), service);
			}
			br.close();
            if (!this.services.isEmpty()) {
                Service.setCount(this.services.values().stream().map(Service::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.serviceFile, false));
            this.services.forEach((k, v) -> pw.println(v.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(int treatmentTypeID, String type, double price, int length) throws Exception {
        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        Service service = new Service(treatmentType, type, price, length, false);
        this.services.put(service.getId(), service);
        // this.priceListManager.add(treatment, type, price);
        this.saveData();
    }

    public void update(int serviceID, int treatmentTypeID, String type, double price, int length) throws Exception {
		Service service = this.findServiceByID(serviceID);
        if (service == null || service.isDeleted()) {
            throw new Exception("Service does not exist.");
        }

        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null || treatmentType.isDeleted()) {
            throw new Exception("Treatment type does not exist.");
        }

        service.setTreatmentType(treatmentType);
        service.setServiceType(type);
        service.setPrice(price);
        service.setLength(length);

		this.saveData();
	}

	public void remove(int serviceID) throws Exception {
        Service service = this.findServiceByID(serviceID);
        if (service == null || service.isDeleted()) {
            throw new Exception("Service does not exist.");
        }
        // this.priceListManager.remove(treatment, type);
        // this.services.remove(serviceID);
        service.delete();

        this.saveData();
	}
	
	private double getEarnings(ScheduledTreatment scheduledTreatment) {
		double earnings = scheduledTreatment.getPrice();
		
		switch(scheduledTreatment.getState()) {
			case SCHEDULED:
				earnings = 0;
				break;
			case CANCELED_SALOON:
				earnings = 0;
				break;
			case CANCELED_CLIENT:
				earnings *= 0.1;
				break;
			default:
				break;
		}
		
		return earnings;
	}
	
	public HashMap<Integer, HashMap<String, Double>> servicesReport(LocalDate startDate, LocalDate endDate) {
		// <serviceID, <"scheduledTreatmentNumber" || "earnings", value>
		HashMap<Integer, HashMap<String, Double>> report = new HashMap<>();
		
		for (Service service : this.services.values()) {
			HashMap<String, Double> serviceReport = new HashMap<>();
			int scheduledTreatments = 0;
			double amountEarned = 0;
			
			for (ScheduledTreatment scheduledTreatment : this.scheduledTreatmentManager.getScheduledTreatments().values().stream().filter(item -> item.getService().getId() == service.getId()).collect(Collectors.toList())) {
				LocalDate scheduledTreatmentDate = scheduledTreatment.getDateTime().toLocalDate();
				if (scheduledTreatmentDate.isAfter(startDate) && scheduledTreatmentDate.isBefore(endDate) || scheduledTreatmentDate.isEqual(startDate) || scheduledTreatmentDate.isEqual(endDate)) {
					scheduledTreatments += 1;
					amountEarned += getEarnings(scheduledTreatment);
				}
			}
			serviceReport.put("scheduledTreatmentNumber", (double) scheduledTreatments);
			serviceReport.put("earnings", amountEarned);
			
			report.put(service.getId(), serviceReport);
		}
		return report;
	}
}
