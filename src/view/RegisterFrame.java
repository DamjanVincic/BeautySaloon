package view;

import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import manage.ManagerFactory;
import manage.UserManager;
import net.miginfocom.swing.MigLayout;

public class RegisterFrame extends JFrame {
	private static final long serialVersionUID = 1165892559091223194L;
	
	private ManagerFactory managerFactory;
	private UserManager userManager;
	
	public RegisterFrame(ManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
		this.userManager = managerFactory.getUserManager();
		
		setTitle("Register");
		setResizable(false);
		setSize(400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[grow][grow][grow][grow][grow][grow][grow][grow][grow]"));
		
		add(new JLabel("Please enter your information to register."), "span, align center");
		
		JTextField usernameField = new JTextField(20);
		add(new JLabel("Username: "));
		add(usernameField);
		
		JPasswordField passwordField = new JPasswordField(20);
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
		
		JButton registerButton = new JButton("Register");
		getRootPane().setDefaultButton(registerButton);
		add(registerButton);
		
		
		cancelButton.addActionListener(e -> {
			dispose();
			MainFrame mainFrame = new MainFrame(this.managerFactory);
			mainFrame.setVisible(true);
		});
		
		registerButton.addActionListener(e -> {
			try {
				userManager.register(nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), usernameField.getText(), String.valueOf(passwordField.getPassword()));
				JOptionPane.showMessageDialog(null, "You have successfully registered.", "", JOptionPane.INFORMATION_MESSAGE);
				
				userManager.login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
				dispose();
				ClientFrame clientFrame = new ClientFrame(this.managerFactory);
				clientFrame.setVisible(true);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
