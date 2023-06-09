package model;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import manage.UserManager;

public class BeauticiansReportModel extends AbstractTableModel {
	private static final long serialVersionUID = -5867328993120676069L;
	
	private String[] columnNames = {"Beautician", "Number of Treatments Done", "Amount Earned"};
	private UserManager userManager;
	private HashMap<Integer, HashMap<String, Double>> beauticiansReport;
	
	
	public BeauticiansReportModel(UserManager userManager, HashMap<Integer, HashMap<String, Double>> beauticiansReport) {
		this.userManager = userManager;
		this.beauticiansReport = beauticiansReport;
	}
	
	
	@Override
	public int getRowCount() {
		return beauticiansReport.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Integer beauticianID = (Integer) beauticiansReport.keySet().toArray()[rowIndex];
		switch(columnIndex) {
			case 0:
				return userManager.findUserById(beauticianID).getUsername();
			case 1:
				return beauticiansReport.get(beauticianID).get("treatmentNumber");
			case 2:
				return beauticiansReport.get(beauticianID).get("earnings");
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
