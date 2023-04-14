package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import entity.Treatment;

public class TreatmentManager {
    private String treatmentFile;
    private ArrayList<Treatment> treatments;

    public TreatmentManager(String treatmentFile) {
        this.treatmentFile = treatmentFile;
        this.treatments = new ArrayList<>();
    }

    public Treatment findTreatmentByType(String type) {
        Treatment treatment;
        try {
            ArrayList<Treatment> filtered = new ArrayList<>(this.treatments.stream()
                                                            .filter(t -> t.getTreatment().equals(type))
                                                            .collect(Collectors.toList()));
            treatment = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            treatment = null;
        }
        return treatment;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.treatmentFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                Treatment treatment = new Treatment(line);
				this.treatments.add(treatment);
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.treatmentFile, false));
            for (Treatment treatment : this.treatments) {
                pw.println(treatment.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String type) throws Exception {
        if (this.findTreatmentByType(type) != null) {
            throw new Exception("Treatment already exists.");
        }
        
        this.treatments.add(new Treatment(type));
        this.saveData();
        
        // System.out.println("Treatment successfully added.");
    }

    // public void update(String type) {
	// 	Treatment treatment = this.findTreatmentByType(type);
    //     if (treatment == null) {
    //         System.out.println("Treatment does not exist.");
    //         return;
    //     }
    //     treatment.setTreatment(type);

	// 	this.saveData();

	// }

	public void remove(String type) throws Exception {
        Treatment treatment = this.findTreatmentByType(type);
        if (treatment == null) {
            throw new Exception("Treatment does not exist.");
        }
        this.treatments.remove(treatment);
        this.saveData();
	}
}
