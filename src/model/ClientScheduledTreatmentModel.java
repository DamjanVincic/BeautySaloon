package model;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Client;
import entity.ScheduledTreatment;
import manage.ScheduledTreatmentManager;

public class ClientScheduledTreatmentModel extends AbstractTableModel {
	private static final long serialVersionUID = -727701068864683871L;

	private String[] columnNames = {"Service", "Treatment Type", "Beautician", "Date & Time", "State", "Price"};
	private List<ScheduledTreatment> scheduledTreatments;
	
	public ClientScheduledTreatmentModel(ScheduledTreatmentManager scheduledTreatmentManager, Client currentUser) {
		// vrati filter posle testiranja
		this.scheduledTreatments = scheduledTreatmentManager.getScheduledTreatments().values().stream().collect(Collectors.toList());
	}
	
	@Override
	public int getRowCount() {
		return this.scheduledTreatments.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ScheduledTreatment scheduledTreatment = this.scheduledTreatments.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return scheduledTreatment.getService().getServiceType();
			case 1:
				return scheduledTreatment.getService().getTreatmentType().getType();
			case 2:
				return scheduledTreatment.getBeautician().getUsername();
			case 3:
				return scheduledTreatment.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
			case 4:
				return scheduledTreatment.getState().getText();
			case 5:
				return scheduledTreatment.getPrice();
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
