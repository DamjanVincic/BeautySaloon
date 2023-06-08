package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Employee;
import entity.Role;
import manage.UserManager;

public class EmployeeModel extends AbstractTableModel {
	private static final long serialVersionUID = 6592489909228395428L;
	
	private String[] columnNames = {"Username", "Name", "Surname", "Gender", "Phone", "Address", "Education Level", "Years of experience", "Salary", "Bonus"};
	private UserManager userManager;
	
	public EmployeeModel(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public List<Employee> getEmployees() {
		return this.userManager.getUsers().values().stream().filter(item -> item instanceof Employee && item.getRole() != Role.MANAGER).map(item -> (Employee) item).collect(Collectors.toList());
	}

	@Override
	public int getRowCount() {
		return getEmployees().size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Employee employee = getEmployees().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return employee.getUsername();
			case 1:
				return employee.getName();
			case 2:
				return employee.getSurname();
			case 3:
				return employee.getGender();
			case 4:
				return employee.getPhone();
			case 5:
				return employee.getAddress();
			case 6:
				return employee.getEducationLevel().getText();
			case 7:
				return employee.getYearsOfExperience();
			case 8:
				return employee.getBaseSalary();
			case 9:
				return employee.getBonus();
			default:
				return null;
		}
	}
	
	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public Employee getEmployee(int rowIndex) {
		return this.getEmployees().get(rowIndex);
	}
}
