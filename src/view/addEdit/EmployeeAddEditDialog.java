package view.addEdit;

import java.awt.Label;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import entity.Beautician;
import entity.EducationLevel;
import entity.Employee;
import entity.Role;
import entity.TreatmentType;
import manage.ManagerFactory;
import model.TreatmentTypeModel;
import net.miginfocom.swing.MigLayout;

public class EmployeeAddEditDialog extends JDialog {
	private static final long serialVersionUID = -4559910744385666351L;

	public EmployeeAddEditDialog(ManagerFactory managerFactory, Employee employee) {
		if (employee == null)
			setTitle("Add employee");
		else
			setTitle("Edit employee");
		setResizable(false);
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[grow][grow][grow][grow][grow][grow][grow][grow][grow]"));
		
		JTextField usernameField = new JTextField(20);
		add(new JLabel("Username: "));
		add(usernameField);
		
		JTextField passwordField = new JTextField(20);
		add(new JLabel("Password: "));
		add(passwordField);
		
		JTextField nameField = new JTextField(20);
		add(new JLabel("Name: "));
		add(nameField);
		
		JTextField surnameField = new JTextField(20);
		add(new JLabel("Surname: "));
		add(surnameField);
		
		JTextField genderField = new JTextField(20);
		add(new JLabel("Gender: "));
		add(genderField);
		
		JTextField phoneField = new JTextField(20);
		add(new JLabel("Phone: "));
		add(phoneField);
		
		JTextField addressField = new JTextField(20);
		add(new JLabel("Address: "));
		add(addressField);
		
		JComboBox<String> roleComboBox = new JComboBox<>();
		for (Role role : Role.values()) {
			if (role != Role.CLIENT)
				roleComboBox.addItem(role.getText());
		}
		JLabel roleLabel = new JLabel("Employee role: ");
		if (employee != null) {
			roleComboBox.setVisible(false);
			roleLabel.setVisible(false);
		}
		add(roleLabel);
		add(roleComboBox);
		
		JComboBox<EducationLevel> educationLevelComboBox = new JComboBox<EducationLevel>();
		for (EducationLevel educationLevel : EducationLevel.values())
			educationLevelComboBox.addItem(educationLevel);
		add(new JLabel("Education Level: "));
		add(educationLevelComboBox);
		
		JTextField yearsofExperienceField = new JTextField(20);
		add(new JLabel("Years of experience: "));
		add(yearsofExperienceField);
		
		JTextField salaryField = new JTextField(20);
		add(new JLabel("Salary: "));
		add(salaryField);
		
		JTable treatmentTypeTable = new JTable(new TreatmentTypeModel(managerFactory.getTreatmentTypeManager()));
		JScrollPane treatmentTypeScrollPane = new JScrollPane(treatmentTypeTable);
		if (employee == null || employee.getRole() != Role.BEAUTICIAN)
			treatmentTypeScrollPane.setVisible(false);
		JLabel treatmentTypeLabel = new JLabel("Treatment types: ");
		treatmentTypeLabel.setVisible(false);
		add(treatmentTypeLabel);
		add(treatmentTypeScrollPane);
		
		add(new Label());
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton, "split 2");
		
		JButton doneButton = new JButton("Done");
		getRootPane().setDefaultButton(doneButton);
		add(doneButton);
		
		if (employee != null) {
			usernameField.setText(employee.getUsername());
			usernameField.setEnabled(false);
			passwordField.setText(employee.getPassword());
			nameField.setText(employee.getName());
			surnameField.setText(employee.getSurname());
			genderField.setText(employee.getGender());
			phoneField.setText(employee.getPhone());
			addressField.setText(employee.getAddress());
			educationLevelComboBox.setSelectedItem(employee.getEducationLevel());
			yearsofExperienceField.setText(((Integer)employee.getYearsOfExperience()).toString());
			salaryField.setText(((Double)employee.getBaseSalary()).toString());
			
			if (employee.getRole() == Role.BEAUTICIAN) {
				HashMap<Integer, TreatmentType> treatmentTypesTrainedFor = ((Beautician)employee).getTreatmentTypesTrainedFor();
				TreatmentTypeModel treatmentTypeModel = (TreatmentTypeModel)treatmentTypeTable.getModel();
				for (int row = 0; row < treatmentTypeModel.getRowCount();row++) {
					if (treatmentTypesTrainedFor.containsKey(treatmentTypeModel.getTreatmentType(row).getId()))
						treatmentTypeTable.getSelectionModel().addSelectionInterval(row, row);
				}
			}
		}
		
		
		roleComboBox.addActionListener(e -> {
			Role role = Role.valueOf(((String)roleComboBox.getSelectedItem()).toUpperCase());
			if (role == Role.BEAUTICIAN) {
				treatmentTypeLabel.setVisible(true);
				treatmentTypeScrollPane.setVisible(true);
			} else {
				treatmentTypeLabel.setVisible(false);
				treatmentTypeScrollPane.setVisible(false);
			}
		});
		
		doneButton.addActionListener(e -> {
			if (employee == null) {
				try {
					managerFactory.getUserManager().validateEmployeeInput(nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), usernameField.getText(), passwordField.getText(), yearsofExperienceField.getText(), salaryField.getText());
					
					HashMap<Integer, TreatmentType> treatmentTypesTrainedFor = new HashMap<>();
					Role role = Role.valueOf(((String)roleComboBox.getSelectedItem()).toUpperCase());
					if (role == Role.BEAUTICIAN) {
						int[] selectedRows = treatmentTypeTable.getSelectedRows();
						if (selectedRows.length != 0) {
							for (int row: selectedRows) {
								TreatmentType selectedTreatmentType = ((TreatmentTypeModel)treatmentTypeTable.getModel()).getTreatmentType(row);
								treatmentTypesTrainedFor.put(selectedTreatmentType.getId(), selectedTreatmentType);
							}
						}
					}
					
					managerFactory.getUserManager().add(nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), usernameField.getText(), passwordField.getText(), (EducationLevel)educationLevelComboBox.getSelectedItem(), Integer.parseInt(yearsofExperienceField.getText()), Double.parseDouble(salaryField.getText()), role, treatmentTypesTrainedFor);
					JOptionPane.showMessageDialog(null, "Successfully added an employee.");
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				try {
					managerFactory.getUserManager().validateEmployeeInput(nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), null, passwordField.getText(), yearsofExperienceField.getText(), salaryField.getText());
					
					HashMap<Integer, TreatmentType> treatmentTypesTrainedFor = new HashMap<>();
					Role role = employee.getRole();
					if (role == Role.BEAUTICIAN) {
						int[] selectedRows = treatmentTypeTable.getSelectedRows();
						if (selectedRows.length != 0) {
							for (int row: selectedRows) {
								TreatmentType selectedTreatmentType = ((TreatmentTypeModel)treatmentTypeTable.getModel()).getTreatmentType(row);
								treatmentTypesTrainedFor.put(selectedTreatmentType.getId(), selectedTreatmentType);
							}
						}
					}
					
					managerFactory.getUserManager().update(employee.getId(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), passwordField.getText(), (EducationLevel)educationLevelComboBox.getSelectedItem(), Integer.parseInt(yearsofExperienceField.getText()), Double.parseDouble(salaryField.getText()), treatmentTypesTrainedFor);
					JOptionPane.showMessageDialog(null, "Successfully added an employee.");
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		cancelButton.addActionListener(e -> {
			dispose();
		});
	}
}
