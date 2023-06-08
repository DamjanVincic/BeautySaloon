package model;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Employee;
import entity.Role;
import manage.UserManager;

public class EmployeeModel extends AbstractTableModel {
	private static final long serialVersionUID = 6592489909228395428L;
	
	private String[] columnNames = {"Role", "Username", "Name", "Surname", "Gender", "Phone", "Address", "Education Level", "Years of experience", "Salary", "Bonus"};
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
				return employee.getRole().getText();
			case 1:
				return employee.getUsername();
			case 2:
				return employee.getName();
			case 3:
				return employee.getSurname();
			case 4:
				return employee.getGender();
			case 5:
				return employee.getPhone();
			case 6:
				return employee.getAddress();
			case 7:
				return employee.getEducationLevel().getText();
			case 8:
				return employee.getYearsOfExperience();
			case 9:
				return new DecimalFormat("#0.00").format(employee.getBaseSalary());
			case 10:
				return new DecimalFormat("#0.00").format(employee.getBonus());
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
