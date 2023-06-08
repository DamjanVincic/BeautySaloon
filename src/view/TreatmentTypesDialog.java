package view;

import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import entity.Client;
import entity.Employee;
import entity.TreatmentType;
import entity.User;
import manage.ManagerFactory;
import model.ClientModel;
import model.EmployeeModel;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;
import view.addEdit.ClientAddEditDialog;
import view.addEdit.EmployeeAddEditDialog;
import view.addEdit.TreatmentTypeAddEditDialog;

public class TreatmentTypesDialog extends JDialog {
	private static final long serialVersionUID = 6011238839058452068L;
	
	private JTable treatmentTypeTable;

	public TreatmentTypesDialog(ManagerFactory managerFactory) {
		setTitle("Treatment Types");
		setResizable(false);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		menuBar.add(actionsMenu);
		
		JMenuItem addMenuItem = new JMenuItem("Add");
		actionsMenu.add(addMenuItem);
		JMenuItem editMenuItem = new JMenuItem("Edit");
		actionsMenu.add(editMenuItem);
		JMenuItem deleteMenuItem = new JMenuItem("Delete");
		actionsMenu.add(deleteMenuItem);
		
		setJMenuBar(menuBar);
		
		this.treatmentTypeTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
		this.treatmentTypeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.treatmentTypeTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane clientTableScrollPane = new JScrollPane(this.treatmentTypeTable);
		add(clientTableScrollPane);
		
		
		addMenuItem.addActionListener(e -> {
			TreatmentTypeAddEditDialog treatmentTypeAddEditDialog = new TreatmentTypeAddEditDialog(managerFactory, null);
			treatmentTypeAddEditDialog.setVisible(true);
			updateTable();
		});
		
		editMenuItem.addActionListener(e -> {
			TreatmentType treatmentType = getSelectedTreatmentType();
			if (treatmentType == null) {
				JOptionPane.showMessageDialog(null, "You must select a treatment type.", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			TreatmentTypeAddEditDialog treatmentTypeAddEditDialog = new TreatmentTypeAddEditDialog(managerFactory, treatmentType);
			treatmentTypeAddEditDialog.setVisible(true);
			updateTable();
		});
		
		deleteMenuItem.addActionListener(e -> {
			TreatmentType treatmentType = getSelectedTreatmentType();
			if (treatmentType != null) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected treatment type?", "Delete confirmation", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					try {
						managerFactory.getTreatmentTypeManager().remove(treatmentType.getId());
						updateTable();
						JOptionPane.showMessageDialog(null, "Treatment type successfully deleted.", "", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "You must select a treatment type.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
	
	private TreatmentType getSelectedTreatmentType() {
		TreatmentType treatmentType = null;
		int row = treatmentTypeTable.getSelectedRow();
		if (row != -1) {
			treatmentType = ((TreatmentTypeModel)treatmentTypeTable.getModel()).getTreatmentType(row);
		}
		return treatmentType;
	}
	
	private void updateTable() {
		((TreatmentTypeModel)treatmentTypeTable.getModel()).fireTableDataChanged();
	}
}
