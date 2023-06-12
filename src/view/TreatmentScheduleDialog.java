package view;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JCalendar;

import entity.Beautician;
import entity.Client;
import entity.Service;
import entity.TreatmentType;
import manage.ManagerFactory;
import model.ServiceModel;
import model.SpecificServicesModel;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class TreatmentScheduleDialog extends JDialog {
	private static final long serialVersionUID = 4667084812832527018L;
	
	private ManagerFactory managerFactory;
	
	private Beautician selectedBeautician = null;
	private Service selectedService;
	private LocalDate selectedDate = null;
	private LocalTime selectedTime;
	
	private JTextField lengthSearch;
	private JTextField priceSearch;
	private RowFilter<Object, Object> serviceTableLengthFilter = null;
	private RowFilter<Object, Object> serviceTablePriceFilter = null;
	private TableRowSorter<AbstractTableModel> treatmentTypeTableSorter = new TableRowSorter<AbstractTableModel>();
	private TableRowSorter<AbstractTableModel> serviceTableSorter = new TableRowSorter<AbstractTableModel>();
	
	public TreatmentScheduleDialog(Object parent, ManagerFactory managerFactory, Client currentUser) {
		this.managerFactory = managerFactory;
		setTitle("Treatment scheduling");
		setResizable(false);
		setSize(600, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JLabel treatmentTypeLabel = new JLabel("Treatment type: ");
		JTextField treatmentTypeSearch = new JTextField(10);
		add(treatmentTypeLabel, "split 6");
		add(treatmentTypeSearch);
		
		JLabel lengthLabel = new JLabel("Length: ");
		lengthLabel.setVisible(false);
		this.lengthSearch = new JTextField(10);
		lengthSearch.setVisible(false);
		add(lengthLabel);
		add(lengthSearch);
		
		JLabel priceLabel = new JLabel("Price: ");
		priceLabel.setVisible(false);
		this.priceSearch = new JTextField(10);
		priceSearch.setVisible(false);
		add(priceLabel);
		add(priceSearch);
		
		JTable treatmentTypesTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
		treatmentTypeTableSorter.setModel((TreatmentTypeModel)treatmentTypesTable.getModel());
		treatmentTypesTable.setRowSorter(treatmentTypeTableSorter);
		treatmentTypesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane treatmentTypeScrollPane = new JScrollPane(treatmentTypesTable);
		treatmentTypeScrollPane.setPreferredSize(new Dimension(200, 100));
		add(treatmentTypeScrollPane, "split 2");
		
		JTable serviceTable = new JTable(new DefaultTableModel());
		serviceTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane serviceTableScrollPane = new JScrollPane(serviceTable);
		serviceTableScrollPane.setPreferredSize(new Dimension(350, 100));
		serviceTableScrollPane.setVisible(false);
		add(serviceTableScrollPane);
		
		JLabel beauticanLabel = new JLabel("Beautician: ");
		beauticanLabel.setVisible(false);
		add(beauticanLabel, "split 2");
		JComboBox<Beautician> beauticiansComboBox = new JComboBox<>();
		beauticiansComboBox.setVisible(false);
		add(beauticiansComboBox);
		
		JCalendar calendar = new JCalendar();
		calendar.setVisible(false);
		add(calendar);
		
		JComboBox<LocalTime> availableTimeComboBox = new JComboBox<>();
		availableTimeComboBox.setVisible(false);
		add(availableTimeComboBox);
		
		JButton scheduleButton = new JButton("Schedule");
		scheduleButton.setVisible(false);
		add(scheduleButton);
		
		
		treatmentTypeSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				changedUpdate(e);
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (treatmentTypeSearch.getText().trim().length() == 0) {
					treatmentTypeTableSorter.setRowFilter(null);
			    } else {
			    	treatmentTypeTableSorter.setRowFilter(RowFilter.regexFilter("(?i)" + treatmentTypeSearch.getText().trim()));
			    }
			}
        });
		
		lengthSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
        });
		
		priceSearch.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateServiceFilter();
			}
        });
		
		
		treatmentTypesTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
                int selectedRow = treatmentTypesTable.getSelectedRow();
                if (selectedRow != -1) {
                    TreatmentType treatmentType = ((TreatmentTypeModel)treatmentTypesTable.getModel()).getTreatmentType(selectedRow);

                    try {
	                    serviceTable.setModel(new SpecificServicesModel(managerFactory.getServiceManager(), treatmentType.getId()));
	                    serviceTable.setRowSelectionInterval(0, 0);
	                    serviceTableScrollPane.setVisible(true);
	                    serviceTableSorter.setModel((SpecificServicesModel)serviceTable.getModel());
	                    serviceTable.setRowSorter(serviceTableSorter);
                    } catch (IllegalArgumentException ex) {
                    	JOptionPane.showMessageDialog(null, "There are currently no services for chosen treatment type.", "", JOptionPane.INFORMATION_MESSAGE);
                    	return;
                    }
                    
                    lengthLabel.setVisible(true);
                    lengthSearch.setVisible(true);
                    priceLabel.setVisible(true);
                    priceSearch.setVisible(true);
                    
                    List<Beautician> trainedBeauticians = managerFactory.getUserManager().getBeauticiansTrainedForTreatmentType(treatmentType);
					beauticiansComboBox.removeAllItems();
					beauticiansComboBox.addItem(null);
					trainedBeauticians.forEach(item -> beauticiansComboBox.addItem(item));
					if (this.selectedDate != null)
						updateAvailableTimeComboBox(availableTimeComboBox);
                }
            }
		});
		
		serviceTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int selectedRow = serviceTable.getSelectedRow();
				if (selectedRow != -1) {
//					int serviceID = ((ServiceModel) serviceTable.getModel()).getService(selectedRow).getId();
//					Service service = ((ServiceModel) serviceTable.getModel()).getService(selectedRow);
					this.selectedService = ((ServiceModel) serviceTable.getModel()).getService(selectedRow);
					
					beauticanLabel.setVisible(true);
					beauticiansComboBox.setVisible(true);
					calendar.setVisible(true);
				}
			}
		});
		
		beauticiansComboBox.addActionListener(e -> {
			this.selectedBeautician = (Beautician) beauticiansComboBox.getSelectedItem();
			if (this.selectedDate != null)
				updateAvailableTimeComboBox(availableTimeComboBox);
		});
		
		calendar.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("calendar")) {
                Date selectedDate = ((Calendar) e.getNewValue()).getTime();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.selectedDate = localDate;

//                List<LocalTime> availableTimes = managerFactory.getUserManager().getAvailableTimesForBeautician(this.selectedBeautician.getId(), localDate, this.selectedService.getLength());
				updateAvailableTimeComboBox(availableTimeComboBox);
            }
		});
		
		availableTimeComboBox.addActionListener(e -> {
			this.selectedTime = (LocalTime) availableTimeComboBox.getSelectedItem();
			scheduleButton.setVisible(true);
		});
		
		scheduleButton.addActionListener(e -> {
			try {
				managerFactory.getScheduledTreatmentManager().scheduleTreatment(currentUser.getId(), this.selectedService.getId(), this.selectedBeautician == null ? null : this.selectedBeautician.getId(), this.selectedDate.atTime(this.selectedTime));
				JOptionPane.showMessageDialog(null, "You have successfully scheduled a treatment.", "", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosed(WindowEvent e) {
				if (parent instanceof ClientFrame)
					((ClientFrame) parent).updateClientAmountSpentLabel();
				if (parent instanceof ReceptionistClientPickerDialog)
					((ReceptionistClientPickerDialog) parent).dispose();
            }
		});
	}
	
	public void updateAvailableTimeComboBox(JComboBox<LocalTime> availableTimeComboBox) {
		List<LocalTime> availableTimes = managerFactory.getUserManager().getAvailableTimesForBeautician(this.selectedBeautician, this.selectedDate, this.selectedService.getLength());
		availableTimeComboBox.removeAllItems();
		availableTimes.forEach(item -> availableTimeComboBox.addItem(item));
		availableTimeComboBox.setVisible(true);
	}
	
	private void updateServiceFilter() {
		if (this.lengthSearch.getText().trim().length() == 0) {
			this.serviceTableLengthFilter = null;
	    } else {
	    	try {
	    		int length = Integer.parseInt(lengthSearch.getText());
	    		
	    		this.serviceTableLengthFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, length + 1, 2);
	    	} catch (Exception ex) {
	    		this.serviceTableLengthFilter = null;
	    	}
	    }
		
		if (priceSearch.getText().trim().length() == 0) {
			this.serviceTablePriceFilter = null;
	    } else {
	    	try {
	    		double price = Double.parseDouble(priceSearch.getText());
	    		
	    		this.serviceTablePriceFilter = RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, price + 1, 1);
	    	} catch (Exception ex) {
	    		this.serviceTablePriceFilter = null;
	    	}
	    }
		
		List<RowFilter<Object, Object>> filters = new ArrayList<>();
		if (this.serviceTableLengthFilter != null)
			filters.add(this.serviceTableLengthFilter);
		if (this.serviceTablePriceFilter != null)
			filters.add(this.serviceTablePriceFilter);
		
		this.serviceTableSorter.setRowFilter(RowFilter.andFilter(filters));
	}
}
