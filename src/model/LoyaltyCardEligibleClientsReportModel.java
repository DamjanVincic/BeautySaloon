package model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import entity.Client;

public class LoyaltyCardEligibleClientsReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 2664307569631136531L;
	
	private String[] columnNames = {"Username", "Loyalty Card Eligibilty"};
	private ArrayList<Client> eligibleClients;
	
	
	public LoyaltyCardEligibleClientsReportModel(ArrayList<Client> eligibleClients) {
		this.eligibleClients = eligibleClients;
	}
	
	
	@Override
	public int getRowCount() {
		return eligibleClients.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Client client = eligibleClients.get(rowIndex);
		switch(columnIndex) {
			case 0:
				return client.getUsername();
			case 1:
				return client.hasLoyaltyCard();
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
}
