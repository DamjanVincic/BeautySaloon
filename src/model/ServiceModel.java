package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.Service;
import manage.ServiceManager;

public class ServiceModel extends AbstractTableModel {
	private static final long serialVersionUID = 5611410755412899701L;

	private String[] columnNames = {"Service", "Price", "Length"};
	private ServiceManager serviceManager;
	private int treatmentTypeID;
	
	public ServiceModel(ServiceManager serviceManager, int treatmentTypeID) {
		this.serviceManager = serviceManager;
		this.treatmentTypeID = treatmentTypeID;
	}
	
	public List<Service> getTreatmentTypeServices() {
		return this.serviceManager.getServices().values().stream().filter(item -> item.getTreatmentType().getId() == treatmentTypeID).collect(Collectors.toList());
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
				return service.getPrice();
			case 2:
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
