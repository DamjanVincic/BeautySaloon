package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import entity.Beautician;
import entity.Client;
import entity.Employee;
import entity.Receptionist;
import entity.Role;
import entity.ScheduledTreatment;
import entity.Service;
import entity.State;
import entity.User;

public class ScheduledTreatmentManager {
    private String scheduledTreatmentFile;
    private UserManager userManager;
    private ServiceManager serviceManager;
    private TreatmentTypeManager treatmentTypeManager;
    // private ArrayList<ScheduledTreatment> scheduledTreatments;
    private HashMap<Integer, ScheduledTreatment> scheduledTreatments;
    private LocalTime saloonStartTime, saloonEndTime;

    public ScheduledTreatmentManager(String scheduledTreatmentFile, UserManager userManager, ServiceManager serviceManager, TreatmentTypeManager treatmentTypeManager, LocalTime saloonStartTime, LocalTime saloonEndTime) {
        this.scheduledTreatmentFile = scheduledTreatmentFile;
        this.userManager = userManager;
        this.serviceManager = serviceManager;
        this.treatmentTypeManager = treatmentTypeManager;
        this.scheduledTreatments = new HashMap<>();
        this.saloonStartTime = saloonStartTime;
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
    			if (beautician.isAvailable(dateTime, service.getLength(), beauticianTreatments, this.saloonStartTime, this.saloonEndTime))
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
	
	public void scheduleTreatment(int clientID, int serviceID, Integer beauticianID, LocalDateTime dateTime) throws Exception {
    	if (dateTime.isBefore(LocalDateTime.now()))
    		throw new Exception("The chosen date and time is in the past.");
		
		Service service = this.serviceManager.findServiceByID(serviceID);
    	
    	if (!isClientAvailable(clientID, service, dateTime))
    		throw new Exception("Client already has a scheduled treatment at that time.");
    	
    	HashMap<Integer, Beautician> availableBeauticians = getAvailableBeauticians(service, dateTime);
    
    	if (availableBeauticians.size() == 0)
    		throw new Exception("There are no available beauticians trained for that treatment type.");
    	
    	if (beauticianID == null) {
    		ArrayList<Beautician> availableBeauticiansValues = new ArrayList<>(availableBeauticians.values());
    		beauticianID = availableBeauticiansValues.get(new Random().nextInt(availableBeauticians.size())).getId();
    	} else {
    		if (!availableBeauticians.containsKey(beauticianID))
    			throw new Exception("There is no beautician with that data.");
    	}
    	this.add(clientID, serviceID, beauticianID, dateTime);
	}
	
	public void cancelTreatment(int scheduledTreatmentID, int userID) {
		User user = this.userManager.findUserById(userID);
		ScheduledTreatment scheduledTreatment = this.findScheduledTreatmentById(scheduledTreatmentID);
		if (user instanceof Client)
			scheduledTreatment.setState(State.CANCELED_CLIENT);
		else if (user instanceof Receptionist)
			scheduledTreatment.setState(State.CANCELED_SALOON);
		
		saveData();
	}
	
	public void changeScheduledTreatmentState(int scheduledTreatmentID, State state) {
		ScheduledTreatment scheduledTreatment = findScheduledTreatmentById(scheduledTreatmentID);
		scheduledTreatment.setState(state);
		saveData();
	}
	
	public HashMap<Integer, HashMap<String, Double>> beauticiansReport(LocalDate startDate, LocalDate endDate) {
		// <beauticianID, <numberOfTreatmentsDone, amountEarned>>
		HashMap<Integer, HashMap<String, Double>> report = new HashMap<>();
		
		for (User user : this.userManager.getUsers().values().stream().filter(item -> item.getRole() == Role.BEAUTICIAN).collect(Collectors.toList())) {
			HashMap<String, Double> beauticianReport = new HashMap<>();
			int numberOfTreatmentsDone = 0;
			double amountEarned = 0;
			Beautician beautician = (Beautician)user;
			
			for (ScheduledTreatment scheduledTreatment : this.scheduledTreatments.values().stream().filter(item -> item.getBeautician().getId() == beautician.getId() && item.getState() == State.COMPLETED).collect(Collectors.toList())) {
				LocalDate scheduledTreatmentDate = scheduledTreatment.getDateTime().toLocalDate();
				if (scheduledTreatmentDate.isAfter(startDate) && scheduledTreatmentDate.isBefore(endDate) || scheduledTreatmentDate.isEqual(startDate) || scheduledTreatmentDate.isEqual(endDate)) {
					numberOfTreatmentsDone++;
					amountEarned += scheduledTreatment.getPrice();
				}
			}
			beauticianReport.put("treatmentNumber", (double) numberOfTreatmentsDone);
			beauticianReport.put("earnings", amountEarned);
			report.put(beautician.getId(), beauticianReport);
		}
		
		return report;
	}
	
	public HashMap<State, Integer> scheduledTreatmentsStateReport(LocalDate startDate, LocalDate endDate) {
		HashMap<State, Integer> scheduledTreatmentsReport = new HashMap<>();
		
		for (ScheduledTreatment scheduledTreatment : this.getScheduledTreatments().values()) {
			LocalDate scheduledTreatmentDate = scheduledTreatment.getDateTime().toLocalDate();
			if (scheduledTreatmentDate.isAfter(startDate) && scheduledTreatmentDate.isBefore(endDate) || scheduledTreatmentDate.isEqual(startDate) || scheduledTreatmentDate.isEqual(endDate))
				scheduledTreatmentsReport.compute(scheduledTreatment.getState(), (k, v) -> (v == null) ? 1 : v + 1);
		}
		
		return scheduledTreatmentsReport;
	}
	
	public double getTreatmentEarnings(ScheduledTreatment scheduledTreatment) {
		double earnings = scheduledTreatment.getPrice();
		
		switch(scheduledTreatment.getState()) {
			case CANCELED_SALOON:
				earnings = 0;
				break;
			case CANCELED_CLIENT:
				earnings *= 0.1;
				break;
			default:
				break;
		}
		
		return earnings;
	}
	
	public HashMap<Integer, ArrayList<Double>> getTreatmentTypesEarningsPerMonth(YearMonth startMonth, YearMonth endMonth) {
		// <Treatment Type ID, list of earnings per month>
		HashMap<Integer, ArrayList<Double>> treatmentTypesEarningsPerMonthReport = new HashMap<>();
		
		YearMonth currentMonth = startMonth;
        while (currentMonth.isBefore(endMonth) || currentMonth.equals(endMonth)) {
            LocalDate startDate = currentMonth.atDay(1), endDate = currentMonth.atEndOfMonth();
            
            HashMap<Integer, Double> monthEarningsPerTreatmentType = new HashMap<>();
            this.treatmentTypeManager.getTreatmentTypes().values().forEach(item -> monthEarningsPerTreatmentType.put(item.getId(), 0.0));
            for (ScheduledTreatment scheduledTreatment : this.getScheduledTreatments().values()) {
            	LocalDate scheduledTreatmentDate = scheduledTreatment.getDateTime().toLocalDate();
            	if (scheduledTreatmentDate.isAfter(startDate) && scheduledTreatmentDate.isBefore(endDate) || scheduledTreatmentDate.isEqual(startDate) || scheduledTreatmentDate.isEqual(endDate))
            		monthEarningsPerTreatmentType.compute(scheduledTreatment.getService().getTreatmentType().getId(), (k, v) -> (v == null) ? getTreatmentEarnings(scheduledTreatment) : v + getTreatmentEarnings(scheduledTreatment));
            }
            
            for (Map.Entry<Integer, Double> entry : monthEarningsPerTreatmentType.entrySet()) {
            	ArrayList<Double> earningsList = treatmentTypesEarningsPerMonthReport.get(entry.getKey());
            	if (earningsList == null) {
            		treatmentTypesEarningsPerMonthReport.put(entry.getKey(), new ArrayList<Double>(Arrays.asList(entry.getValue())));
            	} else {
            		earningsList.add(entry.getValue());
            	}
            }
            
            currentMonth = currentMonth.plusMonths(1);
        }
        
        return treatmentTypesEarningsPerMonthReport;
	}
	
	public double getEarnings(LocalDate startDate, LocalDate endDate) {
		double earnings = 0;
		
		for (ScheduledTreatment scheduledTreatment : this.getScheduledTreatments().values()) {
			LocalDate scheduledTreatmentDate = scheduledTreatment.getDateTime().toLocalDate();
			if (scheduledTreatmentDate.isAfter(startDate) && scheduledTreatmentDate.isBefore(endDate) || scheduledTreatmentDate.isEqual(startDate) || scheduledTreatmentDate.isEqual(endDate))
				earnings += getTreatmentEarnings(scheduledTreatment);
		}
		
		return earnings;
	}
	
	public double getExpenses(LocalDate startDate, LocalDate endDate) {
		int count = 0;

	    YearMonth startYearMonth = YearMonth.from(startDate);
	    YearMonth endYearMonth = YearMonth.from(endDate);

	    YearMonth currentYearMonth = startYearMonth;
	    while (!currentYearMonth.isAfter(endYearMonth)) {
	        LocalDate firstOfMonth = currentYearMonth.atDay(1);
	        if (!firstOfMonth.isBefore(startDate)) {
	            count++;
	        }
	        currentYearMonth = currentYearMonth.plusMonths(1);
	    }

//	    return this.userManager.getUsers().values().stream().filter(item -> item.getRole() == Role.BEAUTICIAN).mapToDouble(item -> ((Beautician) item).calculatePaycheck()*count).reduce(0, (subtotal, item) -> subtotal + item);
	    double expenses = 0;
	    for (User user : this.userManager.getUsers().values()) {
	    	if (user instanceof Employee) {
	    		Employee employee = (Employee) user;
	    		expenses += employee.calculatePaycheck()*count;
	    	}
	    }
	    
	    return expenses;
	}
	
	public double getTotalEarnings() {
//		double earnings = 0;
//		
//		for (ScheduledTreatment scheduledTreatment : this.getScheduledTreatments().values()) {
//			earnings += getTreatmentEarnings(scheduledTreatment);
//		}
		
//		return earnings;
		return this.scheduledTreatments.values().stream().mapToDouble(item -> getTreatmentEarnings(item)).reduce(0, (subtotal, item) -> subtotal + item);
	}
	
	public List<ScheduledTreatment> getBeauticianSchedule(int beauticianID) {
		Beautician beautician = (Beautician) this.userManager.findUserById(beauticianID);	
		return this.scheduledTreatments.values().stream().filter(item -> item.getBeautician().getId() == beautician.getId() && item.getState() == State.SCHEDULED).collect(Collectors.toList());
	}
	
	public List<ScheduledTreatment> getClientTreatments(int clientID) {
		Client client = (Client) this.userManager.findUserById(clientID);
		return this.scheduledTreatments.values().stream().filter(item -> item.getClient().getId() == client.getId()).collect(Collectors.toList());
	}
}
