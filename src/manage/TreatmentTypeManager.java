package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import entity.Treatment;
import entity.TreatmentType;

public class TreatmentTypeManager {
    private String treatmentTypeFile;
    private TreatmentManager treatmentManager;
    private HashMap<Integer, TreatmentType> treatmentTypes;

    public TreatmentTypeManager(String treatmentTypeFile, TreatmentManager treatmentManager) {
        this.treatmentTypeFile = treatmentTypeFile;
        this.treatmentManager = treatmentManager;
        this.treatmentTypes = new HashMap<>();
    }

    public TreatmentType findTreatmentTypeByID(int id) {
        return this.treatmentTypes.get(id);
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.treatmentTypeFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                TreatmentType treatmentType = new TreatmentType(Integer.parseInt(data[0]), treatmentManager.findTreatmentByID(Integer.parseInt(data[1])), data[2], Double.parseDouble(data[3]));
				this.treatmentTypes.put(treatmentType.getId(), treatmentType);
			}
			br.close();
            if (!this.treatmentTypes.isEmpty()) {
                TreatmentType.setCount(this.treatmentTypes.values().stream().map(TreatmentType::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.treatmentTypeFile, false));
            this.treatmentTypes.forEach((k, v) -> pw.println(v.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(int treatmentID, String type, double price) throws Exception {
        Treatment treatment = this.treatmentManager.findTreatmentByID(treatmentID);
        if (treatment == null) {
            throw new Exception("Treatment does not exist.");
        }
        TreatmentType treatmentType = new TreatmentType(treatment, type, price);
        this.treatmentTypes.put(treatmentType.getId(), treatmentType);
        // this.priceListManager.add(treatment, type, price);
        this.saveData();
    }

    public void update(int treatmentTypeID, int treatmentID, String type, double price) throws Exception {
		TreatmentType treatmentType = this.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }

        Treatment treatment = this.treatmentManager.findTreatmentByID(treatmentID);
        if (treatment == null) {
            throw new Exception("Treatment does not exist.");
        }

        treatmentType.setTreatment(treatment);
        treatmentType.setType(type);
        treatmentType.setPrice(price);

		this.saveData();
	}

	public void remove(int treatmentTypeID) throws Exception {
        TreatmentType treatmentType = this.findTreatmentTypeByID(treatmentTypeID);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        // this.priceListManager.remove(treatment, type);
        this.treatmentTypes.remove(treatmentTypeID);

        this.saveData();
	}
}
