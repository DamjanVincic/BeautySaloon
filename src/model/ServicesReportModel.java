package model;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import entity.Service;
import manage.ServiceManager;

public class ServicesReportModel extends AbstractTableModel {
	private static final long serialVersionUID = -6763077765847685530L;
	
	private String[] columnNames = {"Service", "Treatment Type", "Price", "Length", "Number of Treatments", "Amount Earned"};
	private ServiceManager serviceManager;
	private HashMap<Integer, HashMap<String, Double>> servicesReport;
	
	
	public ServicesReportModel(ServiceManager serviceManager, HashMap<Integer, HashMap<String, Double>> servicesReport) {
		this.serviceManager = serviceManager;
		this.servicesReport = servicesReport;
	}
	
	
	@Override
	public int getRowCount() {
		return servicesReport.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Integer serviceID = (Integer) servicesReport.keySet().toArray()[rowIndex];
		Service service = serviceManager.findServiceByID(serviceID);
		switch(columnIndex) {
			case 0:
				return service.getServiceType();
			case 1:
				return service.getTreatmentType().getType();
			case 2:
				return service.getPrice();
			case 3:
				return service.getLength();
			case 4:
				return servicesReport.get(serviceID).get("scheduledTreatmentNumber");
			case 5:
				return servicesReport.get(serviceID).get("earnings");
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
