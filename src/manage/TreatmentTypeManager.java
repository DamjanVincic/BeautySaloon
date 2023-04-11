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
    private ArrayList<TreatmentType> treatmentTypes;

    public TreatmentTypeManager(String treatmentTypeFile, TreatmentManager treatmentManager) {
        this.treatmentTypeFile = treatmentTypeFile;
        this.treatmentManager = treatmentManager;
        this.treatmentTypes = new ArrayList<>();
    }

    public TreatmentType findTreatmentTypeByType(String type) {
        TreatmentType treatmentType;
        try {
            ArrayList<TreatmentType> filtered = new ArrayList<>(this.treatmentTypes.stream()
                                                                .filter(t -> t.getType().equals(type))
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

    public void add(String treatment, String type) throws Exception {
        if (this.findTreatmentTypeByType(type) != null) {
            throw new Exception("Treatment type already exists.");
        }
        
        Treatment treatment_ = treatmentManager.findTreatmentByType(treatment);
        if (treatment_ == null) {
            throw new Exception("Treatment does not exist.");
        }
        this.treatmentTypes.add(new TreatmentType(treatmentManager.findTreatmentByType(treatment), type));
        this.saveData();
        
        // System.out.println("Treatment type successfully added.");
    }

    // public void update(String type) {
	// 	Treatment treatment = this.findTreatmentByType(type);
    //     if (treatment == null) {
    //         System.out.println("Treatment does not exist.");
    //         return;
    //     }
    //     treatment.setTreatment(type);

	// 	this.saveData();

    //     System.out.println("Treatment successfully edited.");
	// }

	public void remove(String type) throws Exception {
        TreatmentType treatmentType = this.findTreatmentTypeByType(type);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        this.treatmentTypes.remove(treatmentType);
        this.saveData();
        // System.out.println("Treatment type successfully deleted.");
	}
}
