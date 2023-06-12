package view;

import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import manage.ManagerFactory;
import manage.UserManager;

public class LoginFrame extends JFrame{
	private static final long serialVersionUID = -7798292689167507569L;
	
	private ManagerFactory managerFactory;
	private UserManager userManager;

	public LoginFrame(ManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
		this.userManager = managerFactory.getUserManager();
		
		setTitle("Login");
		setResizable(false);
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[]30[][]30[]"));
		
		add(new JLabel("Please log in to your account."), "span, align center");
		
		JTextField usernameTextField = new JTextField(20);
		add(new JLabel("Username: "));
		add(usernameTextField);
		
		JPasswordField passwordField = new JPasswordField(20);
		add(new JLabel("Password:"));
		add(passwordField);
		
		add(new Label());
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton, "split 2");
		
		JButton loginButton = new JButton("Login");
		getRootPane().setDefaultButton(loginButton);
		add(loginButton);
		
		
		cancelButton.addActionListener(e -> {
			dispose();
			MainFrame mainFrame = new MainFrame(this.managerFactory);
			mainFrame.setVisible(true);
		});
		
		loginButton.addActionListener(e -> {
			try {
				userManager.login(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
				dispose();

				switch (this.userManager.getCurrentUser().getRole()) {
					case CLIENT:
						ClientFrame clientFrame = new ClientFrame(this.managerFactory);
						clientFrame.setVisible(true);
						break;
					case RECEPTIONIST:
						ReceptionistFrame receptionistFrame = new ReceptionistFrame(this.managerFactory);
						receptionistFrame.setVisible(true);
						break;
					case MANAGER:
						ManagerFrame managerFrame = new ManagerFrame(managerFactory);
						managerFrame.setVisible(true);
						break;
					case BEAUTICIAN:
						BeauticianFrame beauticianFrame = new BeauticianFrame(managerFactory);
						beauticianFrame.setVisible(true);
						break;
					default:
						break;
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
}
