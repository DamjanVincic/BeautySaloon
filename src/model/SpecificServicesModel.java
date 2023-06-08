package model;

import java.util.List;
import java.util.stream.Collectors;

import entity.Service;
import manage.ServiceManager;

public class SpecificServicesModel extends ServiceModel {
	private static final long serialVersionUID = 254714453656916880L;
	
	private String[] columnNames = {"Service", "Price", "Length"};
	private int treatmentTypeID;
	
	public SpecificServicesModel(ServiceManager serviceManager, int treatmentTypeID) {
		super(serviceManager);
		this.treatmentTypeID = treatmentTypeID;
	}
	
	public List<Service> getTreatmentTypeServices() {
		return super.getTreatmentTypeServices().stream().filter(item -> item.getTreatmentType().getId() == treatmentTypeID).collect(Collectors.toList());
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
}
