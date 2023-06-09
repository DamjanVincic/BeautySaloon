package model;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import entity.State;

public class TreatmentReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 7360408000858131258L;
	
	private String[] columnNames = {"State", "Amount"};
	private HashMap<State, Integer> treatmentsReport;
	
	
	public TreatmentReportModel(HashMap<State, Integer> treatmentsReport) {
		this.treatmentsReport = treatmentsReport;
	}
	
	
	@Override
	public int getRowCount() {
		return treatmentsReport.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
//		Integer beauticianID = (Integer) beauticiansReport.keySet().toArray()[rowIndex];
		State state = (State) treatmentsReport.keySet().toArray()[rowIndex];
		switch(columnIndex) {
			case 0:
				return state.getText();
			case 1:
				return treatmentsReport.get(state);
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
