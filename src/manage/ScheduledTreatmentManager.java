package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import entity.Beautician;
import entity.Client;
import entity.Receptionist;
import entity.ScheduledTreatment;
import entity.Service;
import entity.State;
import entity.User;

public class ScheduledTreatmentManager {
    private String scheduledTreatmentFile;
    private UserManager userManager;
    private ServiceManager serviceManager;
    // private ArrayList<ScheduledTreatment> scheduledTreatments;
    private HashMap<Integer, ScheduledTreatment> scheduledTreatments;
    private LocalTime saloonEndTime;

    public ScheduledTreatmentManager(String scheduledTreatmentFile, UserManager userManager, ServiceManager serviceManager, LocalTime saloonEndTime) {
        this.scheduledTreatmentFile = scheduledTreatmentFile;
        this.userManager = userManager;
        this.serviceManager = serviceManager;
        this.scheduledTreatments = new HashMap<>();
        this.saloonEndTime = saloonEndTime;
    }

    public HashMap<Integer, ScheduledTreatment> getScheduledTreatments() {
        return this.scheduledTreatments;
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
                ScheduledTreatment scheduledTreatment = new ScheduledTreatment(Integer.parseInt(data[0]), (Client) userManager.findUserById(Integer.parseInt(data[1])), serviceManager.findServiceByID(Integer.parseInt(data[2])), (Beautician) userManager.findUserById(Integer.parseInt(data[3])), LocalDateTime.parse(data[4], DateTimeFormatter.ofPattern("dd.MM.yyyy. HH")), State.valueOf(data[5]), Double.parseDouble(data[6]));
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
    
    private HashMap<Integer, Beautician> getAvailableBeauticians(Service service, LocalDateTime dateTime) { 	
    	List<Beautician> beauticians = this.userManager.getUsers().values().stream()
																			.filter(user -> user instanceof Beautician)
																			.map(user -> (Beautician) user)
																			.collect(Collectors.toList());
    	HashMap<Integer, Beautician> availableBeauticians = new HashMap<>();
    	for (Beautician beautician : beauticians) {
    		if (beautician.getTreatmentTypesTrainedFor().containsKey(service.getTreatmentType().getId())) {
    			List<ScheduledTreatment> beauticianTreatments = this.scheduledTreatments.values().stream()
    																							.filter(item -> item.getState() == State.SCHEDULED && item.getBeautician().getId() == beautician.getId())
    																							.collect(Collectors.toList());
    			if (beautician.isAvailable(dateTime, service.getLength(), beauticianTreatments, this.saloonEndTime))
    				availableBeauticians.put(beautician.getId(), beautician);
    		}
    	}
    	return availableBeauticians;
    }
    
    private boolean isClientAvailable(int clientID, Service service, LocalDateTime dateTime) {
    	List<ScheduledTreatment> clientTreatments = this.scheduledTreatments.values().stream()
																					.filter(item -> item.getState() == State.SCHEDULED && item.getClient().getId() == clientID)
																					.collect(Collectors.toList());
    	Client client = (Client)this.userManager.findUserById(clientID);
    	return client.isAvailable(dateTime, service.getLength(), clientTreatments);
    }

    public void add(int clientID, int serviceID, Integer beauticianID, LocalDateTime dateTime) throws Exception {
        // ubaci proveru kada user nije kozmeticar ili ne postoji, da li postoji tip usluge, da li je kozmeticar obucen itd
    	
    	// dodela jednog od postojecih kozmeticara ako nije vec prosledjen
    	Service service = this.serviceManager.findServiceByID(serviceID);
    	
    	if (!isClientAvailable(clientID, service, dateTime))
    		throw new Exception("Klijent vec ima zakazan treatman u zadato vreme.");
    	
    	HashMap<Integer, Beautician> availableBeauticians = getAvailableBeauticians(service, dateTime);
    
    	if (availableBeauticians.size() == 0)
    		throw new Exception("Ne postoje slobodni kozmeticari obuceni za dati tip tretmana.");
    	
    	if (beauticianID == null) {
    		ArrayList<Beautician> availableBeauticiansValues = new ArrayList<>(availableBeauticians.values());
    		beauticianID = availableBeauticiansValues.get(new Random().nextInt(availableBeauticians.size())).getId();
    	} else {
    		if (!availableBeauticians.containsKey(beauticianID))
    			throw new Exception("Ne postoji izabrani kozmeticar za unete podatke.");
    	}
    	
        ScheduledTreatment scheduledTreatment = new ScheduledTreatment((Client)this.userManager.findUserById(clientID), this.serviceManager.findServiceByID(serviceID), (Beautician)this.userManager.findUserById(beauticianID), dateTime);
        this.scheduledTreatments.put(scheduledTreatment.getId(), scheduledTreatment);
        this.saveData();
    }

    public void update(int id, int clientID, int serviceID, int beauticianID, LocalDateTime dateTime, double price) throws Exception {
		ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(id);
        if (scheduledTreatment == null) {
            throw new Exception("Scheduled treatment does not exist.");
        }

        scheduledTreatment.setClient((Client)this.userManager.findUserById(clientID));
        scheduledTreatment.setService(this.serviceManager.findServiceByID(serviceID));
        scheduledTreatment.setBeautician((Beautician)this.userManager.findUserById(beauticianID));
        scheduledTreatment.setDateTime(dateTime);
        scheduledTreatment.setPrice(price);

		this.saveData();
	}

	public void remove(int id) throws Exception {
        if (!this.scheduledTreatments.containsKey(id)) {
            throw new Exception("Scheduled treatment does not exist.");
        }
        this.scheduledTreatments.remove(id);
        this.saveData();
	}
	
	public void cancelTreatment(int scheduledTreatmentID, int userID) {
		// nema validacije jer ce se u guiu prikazivati samo validni
		
		User user = this.userManager.findUserById(userID);
		ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(scheduledTreatmentID);
		if (user instanceof Client)
			scheduledTreatment.setState(State.CANCELED_CLIENT);
		else if (user instanceof Receptionist)
			scheduledTreatment.setState(State.CANCELED_SALOON);
		
		saveData();
	}
}
