package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Service;
import manage.ServiceManager;

public class ServiceModel extends AbstractTableModel {
	private static final long serialVersionUID = 5611410755412899701L;

	private String[] columnNames = {"Service", "Treatment Type", "Price", "Length"};
	private ServiceManager serviceManager;
	
	public ServiceModel(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
	
	public List<Service> getTreatmentTypeServices() {
		return this.serviceManager.getServices().values().stream().collect(Collectors.toList());
	}
	
	@Override
	public int getRowCount() {
		return this.getTreatmentTypeServices().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Service service = this.getTreatmentTypeServices().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return service.getServiceType();
			case 1:
				return service.getTreatmentType().getType();
			case 2:
				return service.getPrice();
			case 3:
				return service.getLength();
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
	
	public Service getService(int rowIndex) {
		return this.getTreatmentTypeServices().get(rowIndex);
	}

}
