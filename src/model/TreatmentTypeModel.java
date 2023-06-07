package model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.table.AbstractTableModel;

import entity.TreatmentType;
import manage.TreatmentTypeManager;

public class TreatmentTypeModel extends AbstractTableModel {
	private static final long serialVersionUID = -540665755833447340L;

	private String[] columnNames = {"Treatment Type"};
	private TreatmentTypeManager treatmentTypeManager;
	
	public TreatmentTypeModel(TreatmentTypeManager treatmentTypeManager) {
		this.treatmentTypeManager = treatmentTypeManager;
	}
	
	public List<TreatmentType> getTreatmentTypes() {
		return this.treatmentTypeManager.getTreatmentTypes().values().stream().filter(item -> !item.isDeleted()).collect(Collectors.toList());
	}
	
	@Override
	public int getRowCount() {
		return this.getTreatmentTypes().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TreatmentType treatmentType = this.getTreatmentTypes().get(rowIndex);
		switch (columnIndex) {
			case 0:
				return treatmentType.getType();
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
	
	public TreatmentType getTreatmentType(int rowIndex) {
		return this.getTreatmentTypes().get(rowIndex);
	}

}
