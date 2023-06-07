package view;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import entity.Beautician;
import entity.Client;
import entity.Service;
import entity.TreatmentType;
import manage.ManagerFactory;
import model.ServiceModel;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class TreatmentScheduleDialog extends JDialog {
	private static final long serialVersionUID = 4667084812832527018L;
	
	private ManagerFactory managerFactory;
	
	private Beautician selectedBeautician = null;
	private Service selectedService;
	private LocalDate selectedDate = null;
	private LocalTime selectedTime;
	
	public TreatmentScheduleDialog(JFrame parent, ManagerFactory managerFactory, Client currentUser) {
		this.managerFactory = managerFactory;
		setTitle("Treatment scheduling");
		setResizable(false);
		setSize(600, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
//		TreatmentTypeModel treatmentTypeModel = new TreatmentTypeModel(managerFactory.getTreatmentTypeManager());
		JTable treatmentTypesTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
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
		
		
		treatmentTypesTable.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
                int selectedRow = treatmentTypesTable.getSelectedRow();
                if (selectedRow != -1) {
                    TreatmentType treatmentType = ((TreatmentTypeModel)treatmentTypesTable.getModel()).getTreatmentType(selectedRow);

                    serviceTable.setModel(new ServiceModel(managerFactory.getServiceManager(), treatmentType.getId()));
                    serviceTable.setRowSelectionInterval(0, 0);
                    serviceTableScrollPane.setVisible(true);
                    
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
				((ClientFrame)parent).updateClientAmountSpentLabel();
            }
		});
	}
	
	public void updateAvailableTimeComboBox(JComboBox<LocalTime> availableTimeComboBox) {
		List<LocalTime> availableTimes = managerFactory.getUserManager().getAvailableTimesForBeautician(this.selectedBeautician, this.selectedDate, this.selectedService.getLength());
		availableTimeComboBox.removeAllItems();
		availableTimes.forEach(item -> availableTimeComboBox.addItem(item));
		availableTimeComboBox.setVisible(true);
	}
}
