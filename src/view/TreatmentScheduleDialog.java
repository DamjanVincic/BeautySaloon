package view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import entity.Beautician;
import entity.Client;
import manage.ManagerFactory;
import model.ServiceModel;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class TreatmentScheduleDialog extends JDialog {
	private static final long serialVersionUID = 4667084812832527018L;
	
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
					
//					beauticiansComboBox.setVisible(true);
				}
			}
		});
		
		
	}
}
