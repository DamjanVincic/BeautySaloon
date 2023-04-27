package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entity.Beautician;
import entity.Client;
import entity.EducationLevel;
import entity.Employee;
import entity.Manager;
import entity.Receptionist;
import entity.Role;
import entity.Treatment;
import entity.User;

public class UserManager {
    private String userFile;
    private HashMap<Integer, User> users;
    private TreatmentManager treatmentManager;

    public UserManager(String userFile, TreatmentManager treatmentManager) {
        this.userFile = userFile;
        this.treatmentManager = treatmentManager;
        this.users = new HashMap<>();
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
                        user = new Client(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                        break;
					case BEAUTICIAN:
                        ArrayList<Treatment> treatmentsTrainedFor = new ArrayList<>();
                        try {
                            Stream.of(data[13].split(";")).forEach(e -> treatmentsTrainedFor.add(this.treatmentManager.findTreatmentByID(Integer.parseInt(e))));
                        } catch (IndexOutOfBoundsException ex) { }
                        user = new Beautician(Integer.parseInt(data[0]), data[2], data[3], data[4], data[5], data[6], data[7], data[8], EducationLevel.valueOf(data[9]), Integer.parseInt(data[10]), Double.parseDouble(data[11]), Double.parseDouble(data[12]));
                        ((Beautician) user).setTreatmentsTrainedFor(treatmentsTrainedFor);
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
        if (this.findUserByUsername(username) != null) {
			throw new Exception("User with given username already exists.");
		}
        User client = new Client(name, surname, gender, phone, address, username, password);
        this.users.put(client.getId(), client);
        this.saveData();
    }

	public void add(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, Role role) throws Exception {
		if (this.findUserByUsername(username) != null) {
			throw new Exception("User with given username already exists.");
		}

		User employee = null;
		switch(role) {
			case BEAUTICIAN:
				employee = new Beautician(name, surname, gender, phone, address, username, password, educationLevel, yearsOfExperience, bonus, baseSalary);
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

    public void update(int id, String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary) throws Exception {
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

		this.saveData();
	}

	public void remove(int id) throws Exception {
        if (!this.users.containsKey(id)) {
            throw new Exception("User does not exist.");
        }
        this.users.remove(id);
        this.saveData();
	}
}
