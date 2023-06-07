package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Client;
import entity.Role;
import manage.UserManager;

public class ClientModel extends AbstractTableModel {
	private static final long serialVersionUID = 2259355120910306991L;
	
	private String[] columnNames = {"Username", "Name", "Surname", "Gender", "Phone", "Address"};
	private UserManager userManager;
	
	public ClientModel(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public List<Client> getClients() {
		return this.userManager.getUsers().values().stream().filter(item -> item.getRole() == Role.CLIENT).map(item -> (Client) item).collect(Collectors.toList());
	}

	@Override
	public int getRowCount() {
		return getClients().size();
	}

	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Client client = getClients().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return client.getUsername();
			case 1:
				return client.getName();
			case 2:
				return client.getSurname();
			case 3:
				return client.getGender();
			case 4:
				return client.getPhone();
			case 5:
				return client.getAddress();
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
	
	public Client getClient(int rowIndex) {
		return this.getClients().get(rowIndex);
	}

}
