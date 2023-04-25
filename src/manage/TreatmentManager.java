package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import entity.Treatment;

public class TreatmentManager {
    private String treatmentFile;
    // private ArrayList<Treatment> treatments;
    private HashMap<Integer, Treatment> treatments;

    public TreatmentManager(String treatmentFile) {
        this.treatmentFile = treatmentFile;
        this.treatments = new HashMap<>();
    }

    public Treatment findTreatmentByID(int id) {
        return treatments.get(0);
    }

    public Treatment findTreatmentByType(String type) {
        Treatment treatment;
        try {
            ArrayList<Treatment> filtered = new ArrayList<>(this.treatments.values().stream()
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
                String[] data = line.split(",");
                Treatment treatment = new Treatment(Integer.parseInt(data[0]), data[1]);
				this.treatments.put(treatment.getId(), treatment);
			}
			br.close();
            if (!this.treatments.isEmpty()) {
                Treatment.setCount(this.treatments.values().stream().map(Treatment::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.treatmentFile));
            this.treatments.values().forEach(v -> pw.println(v.toFileString()));
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
        Treatment treatment = new Treatment(type);
        this.treatments.put(treatment.getId(), treatment);
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

	public void remove(int id) throws Exception {
        Treatment treatment = this.findTreatmentByID(id);
        if (treatment == null) {
            throw new Exception("Treatment does not exist.");
        }
        this.treatments.remove(id);
        this.saveData();
	}
}
