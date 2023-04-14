package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import entity.TreatmentType;

public class PriceListManager {
    private String priceFile;
    private HashMap<TreatmentType, Double> prices;
    private TreatmentTypeManager treatmentTypeManager;

    public PriceListManager(String priceFile, TreatmentTypeManager treatmentTypeManager) {
        this.priceFile = priceFile;
        this.prices = new HashMap<>();
        this.treatmentTypeManager = treatmentTypeManager;
    }

    public double findPriceByTreatmentType(String treatment, String type) {
        Double price;
        try {
            price = this.prices.get(treatmentTypeManager.findTreatmentTypeByType(treatment, type));
        } catch (IndexOutOfBoundsException ex) {
            price = null;
        }
        return price;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.priceFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                this.prices.put(this.treatmentTypeManager.findTreatmentTypeByType(data[0], data[1]), Double.parseDouble(data[2]));
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.priceFile));
            this.prices.entrySet().stream().forEach(e -> pw.println(e.getKey().toFileString() + "," + e.getValue()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String treatment, String type, double price) throws Exception {
        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByType(treatment, type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        if (this.prices.containsKey(treatmentType)) {
            throw new Exception("Treatment type already has a price set.");
        }
        this.prices.put(treatmentType, price);
        this.saveData();
    }

    public void update(String treatment, String type, double price) throws Exception {
        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByType(treatment, type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        // if (!this.prices.containsKey(treatmentType)) {
        //     throw new Exception("Treatment type does not have a price set.");
        // }
        this.prices.put(treatmentType, price);

		this.saveData();
	}

	public void remove(String treatment, String type) throws Exception {
        TreatmentType treatmentType = this.treatmentTypeManager.findTreatmentTypeByType(treatment, type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        if (!this.prices.containsKey(treatmentType)) {
            throw new Exception("Treatment type does not have a price set.");
        }
        this.prices.remove(treatmentType);
        this.saveData();
	}
}
