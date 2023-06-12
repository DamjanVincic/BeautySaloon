package model;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.ScheduledTreatment;
import manage.ScheduledTreatmentManager;

public class ScheduledTreatmentModel extends AbstractTableModel{
	private static final long serialVersionUID = -2277616728888477712L;
	
	private String[] columnNames = {"Client", "Service", "Treatment Type", "Beautician", "Date & Time", "State", "Price"};
	private ScheduledTreatmentManager scheduledTreatmentManager;
	
	public ScheduledTreatmentModel(ScheduledTreatmentManager scheduledTreatmentManager) {
		this.scheduledTreatmentManager = scheduledTreatmentManager;
	}
	
	public List<ScheduledTreatment> getTreatments() {
		return this.scheduledTreatmentManager.getScheduledTreatments().values().stream().collect(Collectors.toList());
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
		if (rowIndex == 0)
			return null;
		ScheduledTreatment scheduledTreatment = this.getTreatments().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return scheduledTreatment.getClient().getUsername();
			case 1:
				return scheduledTreatment.getService().getServiceType();
			case 2:
				return scheduledTreatment.getService().getTreatmentType().getType();
			case 3:
				return scheduledTreatment.getBeautician().getName() + " " + scheduledTreatment.getBeautician().getSurname();
			case 4:
				return scheduledTreatment.getDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
			case 5:
				return scheduledTreatment.getState().getText();
			case 6:
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
		if (this.getValueAt(0, columnIndex) == null)
			return Object.class;
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public ScheduledTreatment getScheduledTreatment(int rowIndex) {
		return this.getTreatments().get(rowIndex);
	}
}
