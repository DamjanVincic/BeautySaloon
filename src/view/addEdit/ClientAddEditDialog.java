package view.addEdit;

import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entity.Client;
import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class ClientAddEditDialog extends JDialog{
	private static final long serialVersionUID = 2751686493150635061L;

	public ClientAddEditDialog(ManagerFactory managerFactory, Client client) {
		if (client == null)
			setTitle("Add client");
		else
			setTitle("Edit client");
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
		
		
		add(new Label());
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton, "split 2");
		
		JButton doneButton = new JButton("Done");
		getRootPane().setDefaultButton(doneButton);
		add(doneButton);
		
		if (client != null) {
			usernameField.setText(client.getUsername());
			usernameField.setEnabled(false);
			passwordField.setText(client.getPassword());
			nameField.setText(client.getName());
			surnameField.setText(client.getSurname());
			genderField.setText(client.getGender());
			phoneField.setText(client.getPhone());
			addressField.setText(client.getAddress());
		}
		
		
		doneButton.addActionListener(e -> {
			if (client == null) {
				try {
					managerFactory.getUserManager().register(nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), usernameField.getText(), passwordField.getText());
					JOptionPane.showMessageDialog(null, "Client successfully added.", "", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				try {
					managerFactory.getUserManager().update(client.getId(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), passwordField.getText());
					JOptionPane.showMessageDialog(null, "Client successfully edited.", "", JOptionPane.INFORMATION_MESSAGE);
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
