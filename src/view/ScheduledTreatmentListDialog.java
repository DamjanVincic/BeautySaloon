package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;

import entity.ScheduledTreatment;
import entity.State;
import manage.ManagerFactory;
import model.ScheduledTreatmentModel;
import net.miginfocom.swing.MigLayout;

public class ScheduledTreatmentListDialog extends JDialog {
	private static final long serialVersionUID = -2892273864677984944L;
	
	private TableRowSorter<ScheduledTreatmentModel> tableSorter = new TableRowSorter<>();
	
	private JTextField serviceSearch;
	private JTextField treatmentTypeSearch;
	private JTextField fromPriceSearch;
	private JTextField toPriceSearch;
	
	private RowFilter<Object, Object> tableServiceFilter = null;
	private RowFilter<Object, Object> tableTreatmentTypeFilter = null;
	private RowFilter<Object, Object> tableFromPriceFilter = null;
	private RowFilter<Object, Object> tableToPriceFilter = null;
	
	public ScheduledTreatmentListDialog(ManagerFactory managerFactory) {
		setTitle("All treatments");
		setResizable(false);
		setSize(900, 350);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]30[]"));
		
		JTable table = new JTable(new ScheduledTreatmentModel(managerFactory.getScheduledTreatmentManager()));
		tableSorter.setModel((ScheduledTreatmentModel)table.getModel());
		table.setRowSorter(tableSorter);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(900, 200));
		add(scrollPane);
		
		add(new JLabel("Service: "), "split 8");
		serviceSearch = new JTextField(10);
		add(serviceSearch);
		
		add(new JLabel("Treatment Type: "));
		treatmentTypeSearch = new JTextField(10);
		add(treatmentTypeSearch);
		
		add(new JLabel("From price: "));
		fromPriceSearch = new JTextField(10);
		add(fromPriceSearch);
		
		add(new JLabel("To price: "));
		toPriceSearch = new JTextField(10);
		add(toPriceSearch);
		
		JButton goBackbutton = new JButton("Go Back");
		add(goBackbutton, "split 2");
		
		JButton cancelTreatmentButton = new JButton("Cancel treatment");
		getRootPane().setDefaultButton(cancelTreatmentButton);
		add(cancelTreatmentButton);
		
		goBackbutton.addActionListener(e -> {
			dispose();
		});
		
		cancelTreatmentButton.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				ScheduledTreatment scheduledTreatment = ((ScheduledTreatmentModel)table.getModel()).getScheduledTreatment(row);
				if (scheduledTreatment.getState() != State.SCHEDULED) {
					JOptionPane.showMessageDialog(null, "You can only cancel scheduled treatments.", "", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the treatment?", "Cancel confirmation", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					managerFactory.getScheduledTreatmentManager().cancelTreatment(scheduledTreatment.getId(), managerFactory.getUserManager().getCurrentUser().getId());
					((ScheduledTreatmentModel) table.getModel()).fireTableDataChanged();
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must select a treatment.", "Invalid selection", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		serviceSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
        });
		treatmentTypeSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
        });
		fromPriceSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
        });
		
		toPriceSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateScheduledTreatmentTable();
			}
        });
	}
	
	private void updateScheduledTreatmentTable() {
		if (this.serviceSearch.getText().trim().length() == 0) {
			this.tableServiceFilter = null;
	    } else {
	    	this.tableServiceFilter = RowFilter.regexFilter("(?i)" + serviceSearch.getText().trim(), 1);
	    }
		
		if (this.treatmentTypeSearch.getText().trim().length() == 0) {
			this.tableTreatmentTypeFilter = null;
	    } else {
	    	this.tableTreatmentTypeFilter = RowFilter.regexFilter("(?i)" + treatmentTypeSearch.getText().trim(), 2);
	    }
		
		if (fromPriceSearch.getText().trim().length() == 0) {
			this.tableFromPriceFilter = null;
	    } else {
	    	try {
	    		double price = Double.parseDouble(fromPriceSearch.getText());
	    		
	    		this.tableFromPriceFilter = RowFilter.numberFilter(RowFilter.ComparisonType.AFTER, price - 1, 6);
	    	} catch (Exception ex) {
	    		this.tableFromPriceFilter = null;
	    	}
	    }
		
		if (toPriceSearch.getText().trim().length() == 0) {
			this.tableToPriceFilter = null;
	    } else {
	    	try {
	    		double price = Double.parseDouble(toPriceSearch.getText());
	    		
	    		this.tableToPriceFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, price + 1, 6);
	    	} catch (Exception ex) {
	    		this.tableToPriceFilter = null;
	    	}
	    }
		
		List<RowFilter<Object, Object>> filters = new ArrayList<>();
		if (this.tableServiceFilter != null)
			filters.add(this.tableServiceFilter);
		if (this.tableTreatmentTypeFilter != null)
			filters.add(this.tableTreatmentTypeFilter);
		if (this.tableFromPriceFilter != null)
			filters.add(this.tableFromPriceFilter);
		if (this.tableToPriceFilter != null)
			filters.add(this.tableToPriceFilter);
		
		this.tableSorter.setRowFilter(RowFilter.andFilter(filters));
	}
}
