package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import entity.Treatment;
import entity.TreatmentType;

public class TreatmentTypeManager {
    private String treatmentTypeFile;
    private TreatmentManager treatmentManager;
    private PriceListManager priceListManager;
    private ArrayList<TreatmentType> treatmentTypes;

    public TreatmentTypeManager(String treatmentTypeFile, TreatmentManager treatmentManager) {
        this.treatmentTypeFile = treatmentTypeFile;
        this.treatmentManager = treatmentManager;
        // this.priceListManager = priceListManager;
        this.treatmentTypes = new ArrayList<>();
    }

    public void setPriceListManager(PriceListManager priceListManager) {
        this.priceListManager = priceListManager;
    }

    public TreatmentType findTreatmentTypeByType(String _treatment, String type) {
        Treatment treatment = this.treatmentManager.findTreatmentByType(_treatment);
        TreatmentType treatmentType;
        try {
            ArrayList<TreatmentType> filtered = new ArrayList<>(this.treatmentTypes.stream()
                                                                .filter(t -> t.getTreatment().getTreatment().equals(treatment.getTreatment()) && t.getType().equals(type))
                                                                .collect(Collectors.toList()));
            treatmentType = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            treatmentType = null;
        }
        return treatmentType;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.treatmentTypeFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                TreatmentType treatmentType = new TreatmentType(treatmentManager.findTreatmentByType(data[0]), data[1]);
				this.treatmentTypes.add(treatmentType);
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.treatmentTypeFile, false));
            for (TreatmentType treatmentType : this.treatmentTypes) {
                pw.println(treatmentType.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String treatment, String type, double price) throws Exception {
        if (this.findTreatmentTypeByType(treatment, type) != null) {
            throw new Exception("Treatment type already exists.");
        }
        
        Treatment treatment_ = treatmentManager.findTreatmentByType(treatment);
        if (treatment_ == null) {
            throw new Exception("Treatment does not exist.");
        }
        this.treatmentTypes.add(new TreatmentType(treatmentManager.findTreatmentByType(treatment), type));
        this.priceListManager.add(treatment, type, price);
        this.saveData();
    }

    public void update(String treatment, String type, double price) throws Exception {
		TreatmentType treatmentType = this.findTreatmentTypeByType(treatment, type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        try {
            this.priceListManager.update(treatment, type, price);
        } catch (Exception ex) { } // pass jer ce uvek postojati u ovom slucaju
		this.saveData();
	}

	public void remove(String treatment, String type) throws Exception {
        TreatmentType treatmentType = this.findTreatmentTypeByType(treatment, type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        this.priceListManager.remove(treatment, type);
        this.treatmentTypes.remove(treatmentType);

        this.saveData();
	}
}
