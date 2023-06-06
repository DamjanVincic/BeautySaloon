package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;

import net.miginfocom.swing.MigLayout;

import manage.UserManager;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 3640724167046322832L;
	
	private UserManager userManager;
	
	public MainFrame(UserManager userManager) {
		this.userManager = userManager;
		
		setTitle("Beauty Saloon");
		setSize(300, 150);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		setLayout(new MigLayout("wrap 1", "[grow, center]", "[grow]"));
		
		add(new JLabel("Welcome."));
		
		JButton loginButton = new JButton("Login");
		add(loginButton);
		
		JButton registerButton = new JButton("Register");
		add(registerButton);
		
		loginButton.addActionListener(e -> {
			dispose();
			LoginFrame loginFrame = new LoginFrame(userManager);
			loginFrame.setVisible(true);
		});
		
		registerButton.addActionListener(e -> {
			dispose();
			RegisterFrame registerFrame = new RegisterFrame(userManager);
			registerFrame.setVisible(true);
		});
	}
}
