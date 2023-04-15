package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import entity.CosmeticSaloon;

public class CosmeticSaloonManager {
    private String cosmeticSaloonFile;
    private ArrayList<CosmeticSaloon> cosmeticSaloons;

    public CosmeticSaloonManager(String cosmeticSaloonFile) {
        this.cosmeticSaloonFile = cosmeticSaloonFile;
        this.cosmeticSaloons = new ArrayList<>();
    }

    public CosmeticSaloon findCosmeticSaloonById(int id) {
        CosmeticSaloon cosmeticSaloon;
        try {
            ArrayList<CosmeticSaloon> filtered = new ArrayList<>(this.cosmeticSaloons.stream()
                                                                .filter(cs -> cs.getId() == id)
                                                                .collect(Collectors.toList()));
            cosmeticSaloon = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            cosmeticSaloon = null;
        }
        return cosmeticSaloon;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.cosmeticSaloonFile));
			String line = null;
            int count = 0;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
                CosmeticSaloon cosmeticSaloon = new CosmeticSaloon(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], data[5], data[6]);
				this.cosmeticSaloons.add(cosmeticSaloon);
                count++;
			}
			br.close();
            if (count > 0) {
                CosmeticSaloon.setCount(cosmeticSaloons.stream().map(CosmeticSaloon::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.cosmeticSaloonFile));
            for (CosmeticSaloon cosmeticSaloon : this.cosmeticSaloons) {
                pw.println(cosmeticSaloon.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String clientFilename, String employeeFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) throws Exception {
        this.cosmeticSaloons.add(new CosmeticSaloon(clientFilename, employeeFilename, treatmentFilename, treatmentTypeFilename, scheduledTreatmentFilename, priceFilename));
        this.saveData();
    }

    // public void update(int id, String clientFilename, String employeeFilename, String treatmentFilename, String treatmentTypeFilename, String scheduledTreatmentFilename, String priceFilename) throws Exception {
	// 	CosmeticSaloon cosmeticSaloon = this.findCosmeticSaloonById(id);
    //     if (cosmeticSaloon == null) {
    //         throw new Exception("Cosmetic saloon does not exist.");
    //     }
    //     // ...

	// 	this.saveData();
	// }

	public void remove(int id) throws Exception {
        CosmeticSaloon cosmeticSaloon = this.findCosmeticSaloonById(id);
        if (cosmeticSaloon == null) {
            throw new Exception("Cosmetic saloon does not exist.");
        }
        this.cosmeticSaloons.remove(cosmeticSaloon);
        this.saveData();
	}
}
