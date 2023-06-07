package model;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Client;
import entity.ScheduledTreatment;
import manage.ScheduledTreatmentManager;

public class ClientScheduledTreatmentModel extends AbstractTableModel {
	private static final long serialVersionUID = -727701068864683871L;

	private String[] columnNames = {"Service", "Treatment Type", "Beautician", "Date & Time", "State", "Price"};
	private ScheduledTreatmentManager scheduledTreatmentManager;
	private Client currentUser;
	
	public ClientScheduledTreatmentModel(ScheduledTreatmentManager scheduledTreatmentManager, Client currentUser) {
		this.scheduledTreatmentManager = scheduledTreatmentManager;
		this.currentUser = currentUser;
	}
	
	public List<ScheduledTreatment> getClientTreatments() {
		return this.scheduledTreatmentManager.getClientTreatments(this.currentUser.getId());
	}
	
	@Override
	public int getRowCount() {
		return this.getClientTreatments().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ScheduledTreatment scheduledTreatment = this.getClientTreatments().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return scheduledTreatment.getService().getServiceType();
			case 1:
				return scheduledTreatment.getService().getTreatmentType().getType();
			case 2:
				return scheduledTreatment.getBeautician().getName() + " " + scheduledTreatment.getBeautician().getSurname();
			case 3:
				return scheduledTreatment.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
			case 4:
				return scheduledTreatment.getState().getText();
			case 5:
				return this.scheduledTreatmentManager.getTreatmentEarnings(scheduledTreatment);
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
	
	public ScheduledTreatment getScheduledTreatment(int rowIndex) {
		return this.getClientTreatments().get(rowIndex);
	}

}
