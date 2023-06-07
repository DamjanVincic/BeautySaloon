package model;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.Beautician;
import entity.ScheduledTreatment;
import entity.User;
import manage.ScheduledTreatmentManager;

public class BeauticianScheduledTreatmentModel extends AbstractTableModel {
	private static final long serialVersionUID = -7582428401268462773L;
	
	private String[] columnNames = {"Client", "Service", "Treatment Type", "Date & Time", "State", "Price"};
	
	private ScheduledTreatmentManager scheduledTreatmentManager;
	private Beautician beautician;
	
	public BeauticianScheduledTreatmentModel(ScheduledTreatmentManager scheduledTreatmentManager, User user) {
		this.scheduledTreatmentManager = scheduledTreatmentManager;
		this.beautician = (Beautician) user;
	}
	
	public List<ScheduledTreatment> getTreatments() {
		return this.scheduledTreatmentManager.getBeauticianSchedule(this.beautician.getId());
	}
	
	@Override
	public int getRowCount() {
		return this.getTreatments().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ScheduledTreatment scheduledTreatment = this.getTreatments().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return scheduledTreatment.getClient().getUsername();
			case 1:
				return scheduledTreatment.getService().getServiceType();
			case 2:
				return scheduledTreatment.getService().getTreatmentType().getType();
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
		return this.getTreatments().get(rowIndex);
	}

}
