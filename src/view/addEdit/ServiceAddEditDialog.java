package view.addEdit;

import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import entity.Service;
import entity.TreatmentType;
import manage.ManagerFactory;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class ServiceAddEditDialog extends JDialog {
	private static final long serialVersionUID = -1382601385895461319L;

	public ServiceAddEditDialog(ManagerFactory managerFactory, Service service) {
		if (service == null)
			setTitle("Add service");
		else
			setTitle("Edit service");
		setResizable(false);
		setSize(400, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[][][]"));
		
		JTable treatmentTypeTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
		treatmentTypeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane treatmentTypeScrollPane = new JScrollPane(treatmentTypeTable);
		JLabel treatmentTypeLabel = new JLabel("Treatment type: ");
		add(treatmentTypeLabel);
		add(treatmentTypeScrollPane);
		
		JTextField serviceField = new JTextField(20);
		add(new JLabel("Service: "));
		add(serviceField);
		
		JTextField priceField = new JTextField(20);
		add(new JLabel("Price: "));
		add(priceField);
		
		JTextField lengthField = new JTextField(20);
		add(new JLabel("Length: "));
		add(lengthField);
		
		add(new Label());
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton, "split 2");
		
		JButton doneButton = new JButton("Done");
		getRootPane().setDefaultButton(doneButton);
		add(doneButton);
		
		if (service != null) {
			TreatmentTypeModel treatmentTypeModel = (TreatmentTypeModel)treatmentTypeTable.getModel();
			for (int row = 0; row < treatmentTypeModel.getRowCount();row++) {
				if (treatmentTypeModel.getTreatmentType(row).getId() == service.getTreatmentType().getId()) {
					treatmentTypeTable.getSelectionModel().addSelectionInterval(row, row);
					break;
				}
			}
			
			serviceField.setText(service.getServiceType());
			priceField.setText(((Double)service.getPrice()).toString());
			lengthField.setText(((Integer)service.getLength()).toString());
		}
		
		
		doneButton.addActionListener(e -> {
			try {
				int row = treatmentTypeTable.getSelectedRow();
				if (row == -1)
					throw new Exception("You must select a treatment type.");
				TreatmentType selectedTreatmentType = ((TreatmentTypeModel)treatmentTypeTable.getModel()).getTreatmentType(row);
				
				if (serviceField.getText().isEmpty() || priceField.getText().isEmpty() || lengthField.getText().isEmpty())
					throw new Exception("All fields must be filled.");
				
				double price;
				int length;
				try {
					price = Double.parseDouble(priceField.getText());
				} catch (Exception ex) {
					throw new Exception("Price must be a number.");
				}
				
				try {
					length = Integer.parseInt(lengthField.getText());
				} catch (Exception ex) {
					throw new Exception("Length must be a number.");
				}
				
				if (service == null) {
					managerFactory.getServiceManager().add(selectedTreatmentType.getId(), serviceField.getText(), price, length);
					JOptionPane.showMessageDialog(null, "Service successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					managerFactory.getServiceManager().update(service.getId(), selectedTreatmentType.getId(), serviceField.getText(), price, length);
					JOptionPane.showMessageDialog(null, "Service successfully edited.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		cancelButton.addActionListener(e -> {
			dispose();
		});
	}
}
