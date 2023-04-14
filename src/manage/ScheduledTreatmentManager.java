package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import entity.Beautician;
import entity.Client;
import entity.ScheduledTreatment;
import entity.State;
import entity.TreatmentType;

public class ScheduledTreatmentManager {
    private String scheduledTreatmentFile;
    private ClientManager clientManager;
    private TreatmentTypeManager treatmentTypeManager;
    private EmployeeManager employeeManager;
    private ArrayList<ScheduledTreatment> scheduledTreatments;

    public ScheduledTreatmentManager(String scheduledTreatmentFile, ClientManager clientManager, TreatmentTypeManager treatmentTypeManager, EmployeeManager employeeManager) {
        this.scheduledTreatmentFile = scheduledTreatmentFile;
        this.clientManager = clientManager;
        this.treatmentTypeManager = treatmentTypeManager;
        this.employeeManager = employeeManager;
        this.scheduledTreatments = new ArrayList<>();
    }

    public ScheduledTreatment findScheduledTreatmentById(int id) {
        ScheduledTreatment scheduledTreatment;
        try {
            ArrayList<ScheduledTreatment> filtered = new ArrayList<>(this.scheduledTreatments.stream()
                                                                .filter(st -> st.getId() == id)
                                                                .collect(Collectors.toList()));
            scheduledTreatment = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            scheduledTreatment = null;
        }
        return scheduledTreatment;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.scheduledTreatmentFile));
			String line = null;
            int count = 0;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                ScheduledTreatment scheduledTreatment = new ScheduledTreatment(Integer.parseInt(data[0]), clientManager.findClientByUsername(data[1]), treatmentTypeManager.findTreatmentTypeByType(data[2], data[3]), (Beautician)employeeManager.findEmployeeByUsername(data[4]), LocalDateTime.parse(data[5], DateTimeFormatter.ofPattern("dd.MM.yyyy. HH")), State.valueOf(data[6]), Double.parseDouble(data[7]));
				this.scheduledTreatments.add(scheduledTreatment);
                count++;
			}
			br.close();
            if (count > 0) {
                ScheduledTreatment.setCount(scheduledTreatments.stream().map(ScheduledTreatment::getId).max(Integer::compare).get());
            }
        } catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.scheduledTreatmentFile, false));
            for (ScheduledTreatment scheduledTreatment : this.scheduledTreatments) {
                pw.println(scheduledTreatment.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(Client client, TreatmentType treatmentType, Beautician beautician, LocalDateTime dateTime, double price) throws Exception {
        this.scheduledTreatments.add(new ScheduledTreatment(client, treatmentType, beautician, dateTime, price));
        this.saveData();
    }

    public void update(int id, Client client, TreatmentType treatmentType, Beautician beautician, LocalDateTime dateTime, double price) throws Exception {
		ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(id);
        if (scheduledTreatment == null) {
            throw new Exception("Scheduled treatment does not exist.");
        }
        scheduledTreatment.setClient(client);
        scheduledTreatment.setTreatmentType(treatmentType);
        scheduledTreatment.setBeautician(beautician);
        scheduledTreatment.setDateTime(dateTime);
        scheduledTreatment.setPrice(price); // ?

		this.saveData();
	}

	public void remove(int id) throws Exception {
        ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(id);
        if (scheduledTreatment == null) {
            throw new Exception("Scheduled treatment does not exist.");
        }
        this.scheduledTreatments.remove(scheduledTreatment);
        this.saveData();
	}
}
