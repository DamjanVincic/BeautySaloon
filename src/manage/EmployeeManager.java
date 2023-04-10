package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import entity.Beautician;
import entity.EducationLevel;
import entity.Employee;
import entity.EmployeeRole;
import entity.Manager;
import entity.Receptionist;

public class EmployeeManager {
    private String employeeFile;
    private ArrayList<Employee> employees;

    public EmployeeManager(String employeeFile) {
        this.employeeFile = employeeFile;
        this.employees = new ArrayList<>();
    }


	public Employee findEmployeeByUsername(String username) {
		Employee employee;
        try {
            ArrayList<Employee> filtered = new ArrayList<Employee>(this.employees.stream()
                                                                .filter(e -> e.getUsername().equals(username))
                                                                .collect(Collectors.toList()));
            employee = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
			employee = null;
		}
        return employee;
	}

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.employeeFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				EmployeeRole employeeRole = EmployeeRole.valueOf(data[11]);
				Employee employee = null;
				switch(employeeRole) {
					case BEAUTICIAN:
						employee = new Beautician(data[0], data[1], data[2], data[3], data[4], data[5], data[6], EducationLevel.valueOf(data[7]), Integer.parseInt(data[8]), Double.parseDouble(data[9]), Double.parseDouble(data[10]));
						break;
					case RECEPTIONIST:
						employee = new Receptionist(data[0], data[1], data[2], data[3], data[4], data[5], data[6], EducationLevel.valueOf(data[7]), Integer.parseInt(data[8]), Double.parseDouble(data[9]), Double.parseDouble(data[10]));
						break;
					case MANAGER:
						employee = new Manager(data[0], data[1], data[2], data[3], data[4], data[5], data[6], EducationLevel.valueOf(data[7]), Integer.parseInt(data[8]), Double.parseDouble(data[9]), Double.parseDouble(data[10]));
						break;
					default:
						break;
				}
				this.employees.add(employee);
			}
			br.close();
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.employeeFile, false));
            for (Employee employee : this.employees) {
                pw.println(employee.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}


	public void add(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, EmployeeRole employeeRole) {
		if (this.findEmployeeByUsername(username) != null) {
			System.out.println("Employee with given username already exists.");
			return;
		}

		Employee employee = null;
		switch(employeeRole) {
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

        this.employees.add(employee);
        this.saveData();

		System.out.println("Employee successfully added.");
    }

    public void update(String name, String surname, String gender, String phone, String address, String username, String password, EducationLevel educationLevel, int yearsOfExperience, double bonus, double baseSalary, EmployeeRole employeeRole) {
		Employee employee = this.findEmployeeByUsername(username);
        if (employee == null) {
            System.out.println("Employe does not exist.");
            return;
        }
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
		employee.setEmployeeRole(employeeRole);

		this.saveData();
		System.out.println("Employee successfully edited.");
	}

	public void delete(String username) {
        Employee employee = this.findEmployeeByUsername(username);
        if (employee == null) {
            System.out.println("Employee does not exist.");
            return;
        }
        this.employees.remove(employee);
        this.saveData();
		System.out.println("Employee successfully deleted.");
	}
}
