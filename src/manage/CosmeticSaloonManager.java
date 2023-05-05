package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.HashMap;

import entity.CosmeticSaloon;

public class CosmeticSaloonManager {
    private String cosmeticSaloonFile;
    private HashMap<Integer, CosmeticSaloon> cosmeticSaloons;

    public CosmeticSaloonManager(String cosmeticSaloonFile) {
        this.cosmeticSaloonFile = cosmeticSaloonFile;
        this.cosmeticSaloons = new HashMap<>();
    }

    public CosmeticSaloon findCosmeticSaloonById(int id) {
        return this.cosmeticSaloons.get(id);
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.cosmeticSaloonFile));
			String line = null;
            // int count = 0;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
                CosmeticSaloon cosmeticSaloon = new CosmeticSaloon(Integer.parseInt(data[0]), data[1], LocalTime.parse(data[2]), LocalTime.parse(data[3]), data[4], data[5], data[6], data[7], data[8]);
				this.cosmeticSaloons.put(cosmeticSaloon.getId(), cosmeticSaloon);
                // count++;
			}
			br.close();
            if (!this.cosmeticSaloons.isEmpty()) {
                CosmeticSaloon.setCount(cosmeticSaloons.keySet().stream().max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.cosmeticSaloonFile));
            this.cosmeticSaloons.values().forEach(v -> pw.println(v.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String name, LocalTime startTime, LocalTime endTime, String userFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) {
        CosmeticSaloon cosmeticSaloon = new CosmeticSaloon(name, startTime, endTime, userFilename, treatmentFilename, treatmentTypeFilename, scheduledTreatmentFilename, priceFilename);
        this.cosmeticSaloons.put(cosmeticSaloon.getId(), cosmeticSaloon);
        this.saveData();
    }

    public void update(int id, String name, LocalTime startTime, LocalTime endTime) throws Exception { //, String userFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) throws Exception {
		CosmeticSaloon cosmeticSaloon = this.findCosmeticSaloonById(id);
        if (cosmeticSaloon == null) {
            throw new Exception("Cosmetic saloon does not exist.");
        }
        cosmeticSaloon.setName(name);
        cosmeticSaloon.setStartTime(startTime);
        cosmeticSaloon.setEndTime(endTime);

		this.saveData();
	}

	public void remove(int id) throws Exception {
        if (!this.cosmeticSaloons.containsKey(id)) {
            throw new Exception("Cosmetic saloon does not exist.");
        }
        this.cosmeticSaloons.remove(id);
        this.saveData();
	}
}
