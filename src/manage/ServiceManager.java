package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import entity.TreatmentType;
import entity.Service;

public class ServiceManager {
    private String serviceFile;
    private TreatmentTypeManager treatmentTypeManager;
    private HashMap<Integer, Service> services;

    public ServiceManager(String serviceFile, TreatmentTypeManager treatmentTypeManager) {
        this.serviceFile = serviceFile;
        this.treatmentTypeManager = treatmentTypeManager;
        this.services = new HashMap<>();
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
                Service service = new Service(Integer.parseInt(data[0]), treatmentTypeManager.findTreatmentTypeByID(Integer.parseInt(data[1])), data[2], Double.parseDouble(data[3]));
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

    public void add(int treatmentTypeID, String type, double price) throws Exception {
        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        Service service = new Service(treatmentType, type, price);
        this.services.put(service.getId(), service);
        // this.priceListManager.add(treatment, type, price);
        this.saveData();
    }

    public void update(int serviceID, int treatmentTypeID, String type, double price) throws Exception {
		Service service = this.findServiceByID(serviceID);
        if (service == null) {
            throw new Exception("Service does not exist.");
        }

        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }

        service.setTreatmentType(treatmentType);
        service.setServiceType(type);
        service.setPrice(price);

		this.saveData();
	}

	public void remove(int serviceID) throws Exception {
        Service service = this.findServiceByID(serviceID);
        if (service == null) {
            throw new Exception("Service does not exist.");
        }
        // this.priceListManager.remove(treatment, type);
        this.services.remove(serviceID);

        this.saveData();
	}
}
