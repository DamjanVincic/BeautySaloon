package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import entity.TreatmentType;

public class TreatmentTypeManager {
    private String treatmentTypeFile;
    private HashMap<Integer, TreatmentType> treatmentTypes;

    public TreatmentTypeManager(String treatmentTypeFile) {
        this.treatmentTypeFile = treatmentTypeFile;
        this.treatmentTypes = new HashMap<>();
    }

    public TreatmentType findTreatmentTypeByID(int id) {
        return treatmentTypes.get(id);
    }

    public TreatmentType findTreatmentTypeByType(String type) {
        TreatmentType treatmentType;
        try {
            ArrayList<TreatmentType> filtered = new ArrayList<>(this.treatmentTypes.values().stream()
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
                TreatmentType treatmentType = new TreatmentType(Integer.parseInt(data[0]), data[1]);
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
			PrintWriter pw = new PrintWriter(new FileWriter(this.treatmentTypeFile));
            this.treatmentTypes.values().forEach(v -> pw.println(v.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String type) throws Exception {
        if (this.findTreatmentTypeByType(type) != null) {
            throw new Exception("Treatment type already exists.");
        }
        TreatmentType treatmentType = new TreatmentType(type);
        this.treatmentTypes.put(treatmentType.getId(), treatmentType);
        this.saveData();
    }

    public void update(int id, String type) throws Exception {
		TreatmentType treatmentType = this.findTreatmentTypeByID(id);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        treatmentType.setType(type);
		this.saveData();
	}

	public void remove(int id) throws Exception {
        TreatmentType treatmentType = this.findTreatmentTypeByID(id);
        if (treatmentType == null) {
            throw new Exception("Treatment type does not exist.");
        }
        this.treatmentTypes.remove(id);
        this.saveData();
	}
}
