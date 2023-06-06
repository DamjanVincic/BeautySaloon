package view;

import java.awt.Dimension;
import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import manage.UserManager;

public class LoginFrame extends JFrame{
	private static final long serialVersionUID = -7798292689167507569L;
	
	private UserManager userManager;

	public LoginFrame(UserManager userManager) {
		this.userManager = userManager;
		
		setTitle("Login");
		setResizable(false);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[grow][grow][grow][grow]"));
		
		getContentPane().add(new JLabel("Please log in to your account."), "span, align center");
		
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
			new MainFrame(this.userManager).setVisible(true);
		});
		
		loginButton.addActionListener(e -> {
			try {
				userManager.login(usernameTextField.getText(), String.valueOf(passwordField.getPassword()));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
	}
}
