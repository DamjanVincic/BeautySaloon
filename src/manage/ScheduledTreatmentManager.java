package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import entity.Beautician;
import entity.Client;
import entity.ScheduledTreatment;
import entity.State;

public class ScheduledTreatmentManager {
    private String scheduledTreatmentFile;
    private UserManager userManager;
    private TreatmentTypeManager treatmentTypeManager;
    // private ArrayList<ScheduledTreatment> scheduledTreatments;
    private HashMap<Integer, ScheduledTreatment> scheduledTreatments;

    public ScheduledTreatmentManager(String scheduledTreatmentFile, UserManager userManager, TreatmentTypeManager treatmentTypeManager) {
        this.scheduledTreatmentFile = scheduledTreatmentFile;
        this.userManager = userManager;
        this.treatmentTypeManager = treatmentTypeManager;
        this.scheduledTreatments = new HashMap<>();
    }

    public ScheduledTreatment findScheduledTreatmentById(int id) {
        return this.scheduledTreatments.get(id);
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.scheduledTreatmentFile));
			String line = null;
            // int count = 0;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                ScheduledTreatment scheduledTreatment = new ScheduledTreatment(Integer.parseInt(data[0]), (Client) userManager.findUserById(Integer.parseInt(data[1])), treatmentTypeManager.findTreatmentTypeByID(Integer.parseInt(data[2])), (Beautician) userManager.findUserById(Integer.parseInt(data[3])), LocalDateTime.parse(data[4], DateTimeFormatter.ofPattern("dd.MM.yyyy. HH")), State.valueOf(data[5]), Double.parseDouble(data[6]));
				this.scheduledTreatments.put(scheduledTreatment.getId(), scheduledTreatment);
                // count++;
			}
			br.close();
            if (!this.scheduledTreatments.isEmpty()) {
                ScheduledTreatment.setCount(scheduledTreatments.keySet().stream().max(Integer::compare).get());
            }
        } catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.scheduledTreatmentFile, false));
            this.scheduledTreatments.values().forEach(v -> pw.println(v.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(int clientID, int treatmentTypeID, int beauticianID, LocalDateTime dateTime, double price) throws Exception {
        ScheduledTreatment scheduledTreatment = new ScheduledTreatment((Client)this.userManager.findUserById(clientID), this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID), (Beautician)this.userManager.findUserById(beauticianID), dateTime, price);
        this.scheduledTreatments.put(scheduledTreatment.getId(), scheduledTreatment);
        this.saveData();
    }

    public void update(int id, int clientID, int treatmentTypeID, int beauticianID, LocalDateTime dateTime, double price) throws Exception {
		ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(id);
        if (scheduledTreatment == null) {
            throw new Exception("Scheduled treatment does not exist.");
        }

        scheduledTreatment.setClient((Client)this.userManager.findUserById(clientID));
        scheduledTreatment.setTreatmentType(this.treatmentTypeManager.findTreatmentTypeByID(treatmentTypeID));
        scheduledTreatment.setBeautician((Beautician)this.userManager.findUserById(beauticianID));
        scheduledTreatment.setDateTime(dateTime);
        scheduledTreatment.setPrice(price); // ?

		this.saveData();
	}

	public void remove(int id) throws Exception {
        if (!this.scheduledTreatments.containsKey(id)) {
            throw new Exception("Scheduled treatment does not exist.");
        }
        this.scheduledTreatments.remove(id);
        this.saveData();
	}
}
