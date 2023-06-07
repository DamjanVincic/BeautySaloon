package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entity.Beautician;
import entity.Client;
import entity.EducationLevel;
import entity.Employee;
import entity.Manager;
import entity.Receptionist;
import entity.Role;
import entity.ScheduledTreatment;
import entity.TreatmentType;
import entity.User;
import entity.State;

public class UserManager {
    private String userFile;
    private HashMap<Integer, User> users;
    private TreatmentTypeManager treatmentTypeManager;
    private ScheduledTreatmentManager scheduledTreatmentManager;
    private LocalTime saloonStartTime, saloonEndTime;
    private User currentUser = null;

    public UserManager(String userFile, TreatmentTypeManager treatmentTypeManager, LocalTime saloonStartTime, LocalTime saloonEndTime) {
        this.userFile = userFile;
        this.treatmentTypeManager = treatmentTypeManager;
        this.saloonStartTime = saloonStartTime;
        this.saloonEndTime = saloonEndTime;
        this.users = new HashMap<>();
    }
    
    public void setScheduledTreatmentManager(ScheduledTreatmentManager scheduledTreatmentManager) {
    	this.scheduledTreatmentManager = scheduledTreatmentManager;
    }

    public HashMap<Integer, User> getUsers() {
        return this.users;
    }

	public User findUserById(int id) {
        return this.users.get(id);
	}

    public User findUserByUsername(String username) {
        User user;
        try {
            ArrayList<User> filtered = new ArrayList<>(this.users.values().stream()
                                                            .filter(c -> c.getUsername().equals(username))
                                                            .collect(Collectors.toList()));
            user = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            user = null;
        }
        return user;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.userFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				Role role = Role.valueOf(data[1]);
				User user = null;
				switch(role) {
                    case CLIENT:
                        user = new Client(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8], Boolean.parseBoolean(data[9]));
                        break;
					case BEAUTICIAN:
                        HashMap<Integer, TreatmentType> treatmentTypesTrainedFor = new HashMap<>();
                        try {
                            Stream.of(data[13].split(";")).forEach(e -> treatmentTypesTrainedFor.put(Integer.parseInt(e), this.treatmentTypeManager.findTreatmentTypeByID(Integer.parseInt(e))));
                        } catch (IndexOutOfBoundsException ex) { }
                        user = new Beautician(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8], EducationLevel.valueOf(data[9]), Integer.parseInt(data[10]), Double.parseDouble(data[11]), Double.parseDouble(data[12]), treatmentTypesTrainedFor);
                        break;
					case RECEPTIONIST:
						user = new Receptionist(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8],  EducationLevel.valueOf(data[9]), Integer.parseInt(data[10]), Double.parseDouble(data[11]), Double.parseDouble(data[12]));
                        break;
					case MANAGER:
						user = new Manager(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8],  EducationLevel.valueOf(data[9]), Integer.parseInt(data[10]), Double.parseDouble(data[11]), Double.parseDouble(data[12]));
						break;
					default:
						break;
				}
				this.users.put(user.getId(), user);
			}
			br.close();
            if (!this.users.isEmpty()) {
                User.setCount(this.users.values().stream().map(User::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.userFile, false));
            this.users.values().forEach(e -> pw.println(e.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}


    public void add(String name, String surname, String gender, String phone, String address, String username, String password) throws Exception {
        User client = new Client(name, surname, gender, phone, address, username, password);
        this.users.put(client.getId(), client);
        this.saveData();
    }

	public void add(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, Role role, HashMap<Integer, TreatmentType> treatmentTypesTrainedFor) throws Exception {
		if (this.findUserByUsername(username) != null) {
			throw new Exception("User with given username already exists.");
		}

		User employee = null;
		switch(role) {
			case BEAUTICIAN:
				// ako se ne izabere ni jedan tip tretmana moze da se prosledi prazna lista i imace isti efekat kao da nije ni prosledjena
				if (treatmentTypesTrainedFor == null)
					employee = new Beautician(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
				else
					employee = new Beautician(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary, treatmentTypesTrainedFor);
				break;
			case RECEPTIONIST:
				employee = new Receptionist(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
				break;
			case MANAGER:
				employee = new Manager(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
				break;
			default:
				break;
		}

        this.users.put(employee.getId(), employee);
        this.saveData();
    }

    public void update(int id, String name, String surname, String gender, String phone, String address, String username, String password) throws Exception {
        User user = this.findUserById(id);
        if (user == null) {
            throw new Exception("User does not exist.");
        }
        if (user.getRole() != Role.CLIENT) {
            throw new Exception("User is not a client.");
        }

        user.setName(name);
        user.setSurname(surname);
        user.setGender(gender);
        user.setPhone(phone);
        user.setAddress(address);
        user.setUsername(username);
        user.setPassword(password);

        this.saveData();
    }

    public void update(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, ArrayList<Integer> treatmentTypesIDs) throws Exception {
		User user = this.findUserById(id);
        if (user == null) {
            throw new Exception("User does not exist.");
        }
        if (user.getRole() == Role.CLIENT) {
            throw new Exception("User is not an employee.");
        }

        Employee employee = (Employee) user;

        employee.setName(name);
        employee.setSurname(surname);
        employee.setGender(gender);
        employee.setPhone(phone);
        employee.setAddress(address);
        employee.setUsername(username);
        employee.setPassword(password);
		employee.setEducationLevel(educationLevel);
		employee.setYearsOfExperience(yearsOfExperience);
		employee.setBonus(bonus);
		employee.setBaseSalary(baseSalary);

        if (treatmentTypesIDs != null) {
            HashMap<Integer, TreatmentType> treatmentTypesTrainedFor = (HashMap<Integer, TreatmentType>)treatmentTypesIDs.stream()
                                                                                    .collect(Collectors.toMap(e -> e, e -> this.treatmentTypeManager.findTreatmentTypeByID(e)));
            ((Beautician) employee).setTreatmentTypesTrainedFor(treatmentTypesTrainedFor);
        }

		this.saveData();
	}

	public void remove(int id) throws Exception {
        if (!this.users.containsKey(id)) {
            throw new Exception("User does not exist.");
        }
        this.users.remove(id);
        this.saveData();
	}
	
	
	private double getClientMoneySpent(Client client) {
//		double spent = 0;
//		List<ScheduledTreatment> clientTreatments = this.scheduledTreatmentManager.getScheduledTreatments().values().stream()
//																													.filter(item -> item.getClient().getId() == client.getId())
//																													.collect(Collectors.toList());
//		for (ScheduledTreatment scheduledTreatment : clientTreatments) {
//			if (scheduledTreatment.getState() == State.SCHEDULED || scheduledTreatment.getState() == State.COMPLETED || scheduledTreatment.getState() == State.NOT_SHOWED_UP)
//				spent += scheduledTreatment.getPrice();
//			if (scheduledTreatment.getState() == State.CANCELED_CLIENT)
//				spent += scheduledTreatment.getPrice() * 0.1;
//		}
//		
//		return spent;
		return this.scheduledTreatmentManager.getScheduledTreatments().values().stream().filter(item -> item.getClient().getId() == client.getId()).mapToDouble(item -> this.scheduledTreatmentManager.getTreatmentEarnings(item)).reduce(0, (subtotal, item) -> subtotal + item);
	}
	
	public void setLoyaltyCardThreshold(double threshold) {
		for (User user : this.getUsers().values()) {
			if (user instanceof Client) {
				Client client = (Client)user;
				if (getClientMoneySpent(client) >= threshold)
					client.setLoyaltyCard(true);
				else
					client.setLoyaltyCard(false);
			}
		}
		saveData();
	}
	
	public ArrayList<Client> getLoyaltyCardEligibleClients() {
		return new ArrayList<>(this.users.values().stream().filter(item -> item.getRole() == Role.CLIENT && ((Client)item).hasLoyaltyCard()).map(item -> (Client)item).collect(Collectors.toList()));
	}
	
	public double getClientAmountSpent(int clientID) {
		Client client = (Client) this.findUserById(clientID);
//		return this.scheduledTreatmentManager.getScheduledTreatments().values().stream().filter(item -> item.getClient().getId() == client.getId()).mapToDouble(item -> this.scheduledTreatmentManager.getTreatmentEarnings(item)).reduce(0, (subtotal, item) -> subtotal + item);
		return getClientMoneySpent(client);
	}
	
	// menadzer postavlja bonus na kraju svakog meseca
	public void setBonusRequirement(int treatmentsCompletedThreshold, double bonus) {
		for (User user : this.getUsers().values()) {
			if (user instanceof Beautician) {
				Beautician beautician = (Beautician) user;
				int treatmentsCompleted = this.scheduledTreatmentManager.getScheduledTreatments().values().stream().filter(item -> item.getBeautician().getId() == beautician.getId() && item.getState() == State.COMPLETED).collect(Collectors.toList()).size();
				
				if (treatmentsCompleted >= treatmentsCompletedThreshold)
					beautician.setBonus(bonus);
			}
		}
		saveData();
	}
	
	public User getCurrentUser() {
		return this.currentUser;
	}
	
	public void login(String username, String password) throws Exception {
		if (username.isEmpty() || password.isEmpty())
			throw new Exception("All fields must be filled.");
		
		User user = this.findUserByUsername(username);
		if (user == null)
			throw new Exception("User with the given username doesn't exist.");
		
		if (user.getPassword().equals(password)) {
			this.currentUser = user;
		}
		else {
			throw new Exception("Wrong password.");
		}
	}
	
	public void logout() {
		this.currentUser = null;
	}
	
	public void register(String name, String surname, String gender, String phone, String address, String username, String password) throws Exception {
		if (name.isEmpty() || surname.isEmpty() || gender.isEmpty() || phone.isEmpty() || address.isEmpty() || username.isEmpty() || password.isEmpty())
			throw new Exception("All fields must be filled.");
		
		if (this.findUserByUsername(username) != null) {
			throw new Exception("User with that username already exists.");
		}
		
		
		if (!name.matches("^[a-zA-Z ]+$"))
			throw new Exception("Name can contain only letters and spaces.");
		if (!surname.matches("^[a-zA-Z ]+$"))
			throw new Exception("Surname can only contain letters and spaces.");
		if (!phone.matches("^[\\d\\s-]+$"))
			throw new Exception("Phone number can only contain numbers, spaces and dashes.");
		if (!address.matches("^[\\w\\d\\s.'-]+$"))
			throw new Exception("Invalid address input.");
		if (!username.matches("^[\\w.]+$"))
			throw new Exception("Invalid username input.");
		if (!password.matches("^[^,]{8,}$"))
			throw new Exception("Password must have at least 8 characters.");
		
		add(name, surname, gender, phone, address, username, password);
	}
	
	public List<Beautician> getBeauticiansTrainedForTreatmentType(int treatmentTypeID) {
		return this.users.values().stream().filter(item -> item.getRole() == Role.BEAUTICIAN && ((Beautician)item).isTrainedForTreatmentType(treatmentTypeID)).map(item -> (Beautician)item).collect(Collectors.toList());
	}
	
	public List<LocalDateTime> getAvailableTimeForBeautician(int beauticianID, LocalDate date, int duration) {
		Beautician beautician = (Beautician) this.findUserById(beauticianID);
		List<LocalDateTime> availableTimes = new ArrayList<>();
		
		for (LocalTime localTime = saloonStartTime; localTime.isBefore(saloonEndTime); localTime = localTime.plusHours(1)) {
			LocalDateTime localDateTime = date.atTime(localTime);
			if (beautician.isAvailable(localDateTime, duration, this.scheduledTreatmentManager.getBeauticianSchedule(beauticianID), saloonStartTime, saloonEndTime))
				availableTimes.add(localDateTime);
		}
		
		return availableTimes;
	}
}
