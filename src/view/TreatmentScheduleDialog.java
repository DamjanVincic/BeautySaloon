package view;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import entity.Beautician;
import entity.Client;
import entity.Service;
import manage.ManagerFactory;
import model.ServiceModel;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class TreatmentScheduleDialog extends JDialog {
	private static final long serialVersionUID = 4667084812832527018L;
	
	private Beautician selectedBeautician;
	private Service selectedService;
	private LocalDate selectedDate;
	private LocalTime selectedTime;
	
	public TreatmentScheduleDialog(ManagerFactory managerFactory, Client currentUser) {
		setTitle("Treatment scheduling");
		setResizable(false);
		setSize(800, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]"));
		
//		TreatmentTypeModel treatmentTypeModel = new TreatmentTypeModel(managerFactory.getTreatmentTypeManager());
		JTable treatmentTypesTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
		treatmentTypesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane treatmentTypeScrollPane = new JScrollPane(treatmentTypesTable);
		treatmentTypeScrollPane.setPreferredSize(new Dimension(200, 100));
		add(treatmentTypeScrollPane);
		
		JTable serviceTable = new JTable(new DefaultTableModel());
		serviceTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane serviceTableScrollPane = new JScrollPane(serviceTable);
		serviceTableScrollPane.setPreferredSize(new Dimension(300, 100));
		serviceTableScrollPane.setVisible(false);
		add(serviceTableScrollPane);
		
		JComboBox<Beautician> beauticiansComboBox = new JComboBox<>();
		beauticiansComboBox.setVisible(false);
		add(beauticiansComboBox);
		
		JCalendar calendar = new JCalendar();
		calendar.setVisible(false);
		add(calendar);
		
		JComboBox<LocalTime> availableTimeComboBox = new JComboBox<>();
		availableTimeComboBox.setVisible(false);
		add(availableTimeComboBox);
		
		// ovde Label "Chosen date: <datum>" ?
		
		// ovde combo box sa dostupnim terminima za uneti datum (mozda preci na JDatePicker umesto JCalendar?)
		
		JButton scheduleButton = new JButton("Schedule");
		scheduleButton.setVisible(false);
		add(scheduleButton);
		
		
		treatmentTypesTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
                int selectedRow = treatmentTypesTable.getSelectedRow();
                if (selectedRow != -1) {
                    int treatmentTypeID = ((TreatmentTypeModel)treatmentTypesTable.getModel()).getTreatmentType(selectedRow).getId();

                    serviceTable.setModel(new ServiceModel(managerFactory.getServiceManager(), treatmentTypeID));
                    serviceTableScrollPane.setVisible(true);
                    
                    List<Beautician> trainedBeauticians = managerFactory.getUserManager().getBeauticiansTrainedForTreatmentType(treatmentTypeID);
					beauticiansComboBox.removeAllItems();
					trainedBeauticians.forEach(item -> beauticiansComboBox.addItem(item));
					beauticiansComboBox.setVisible(true);
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
					
//					beauticiansComboBox.setVisible(true);
				}
			}
		});
		
		beauticiansComboBox.addActionListener(e -> {
			this.selectedBeautician = (Beautician) beauticiansComboBox.getSelectedItem();
			calendar.setVisible(true);
		});
		
		calendar.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("calendar")) {
                Date selectedDate = ((Calendar) e.getNewValue()).getTime();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.selectedDate = localDate;

                List<LocalDateTime> availableTimes = managerFactory.getUserManager().getAvailableTimeForBeautician(this.selectedBeautician.getId(), localDate, this.selectedService.getId());
				availableTimeComboBox.removeAllItems();
				availableTimes.forEach(item -> availableTimeComboBox.addItem(item.toLocalTime()));
				availableTimeComboBox.setVisible(true);
            }
		});
		
		availableTimeComboBox.addActionListener(e -> {
			this.selectedTime = (LocalTime) availableTimeComboBox.getSelectedItem();
			scheduleButton.setVisible(true);
		});
		
		scheduleButton.addActionListener(e -> {
			try {
				managerFactory.getScheduledTreatmentManager().scheduleTreatment(currentUser.getId(), this.selectedService.getId(), this.selectedBeautician.getId(), this.selectedDate.atTime(this.selectedTime));
				JOptionPane.showMessageDialog(null, "You have successfully scheduled a treatment.", "", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
