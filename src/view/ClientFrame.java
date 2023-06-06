package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import entity.Client;
import entity.User;
import manage.UserManager;
import net.miginfocom.swing.MigLayout;

public class ClientFrame extends JFrame {
	private static final long serialVersionUID = 6452299993070588719L;
	
	private UserManager userManager;
	private Client currentUser;
	
	public ClientFrame(UserManager userManager) {
		this.userManager = userManager;
		this.currentUser = (Client) userManager.getCurrentUser();
		
		setTitle("Client Panel");
		setResizable(false);
		setSize(400, 275);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[][]30[][]30[]"));
		
		add(new JLabel(String.format("Welcome, %s!", this.currentUser.getUsername())));
		add(new JLabel(String.format("Current loyalty card balance: %.2f", this.userManager.getClientAmountSpent(this.currentUser.getId()))));
		add(new JLabel(String.format("Status: %s", this.currentUser.hasLoyaltyCard() ? "Acquired" : "Not Acquired")));
	
		JButton scheduleTreatmentButton = new JButton("Schedule a treatment");
		add(scheduleTreatmentButton);
		
		JButton scheduledTreatmentListButton = new JButton("Your scheduled treatments");
		add(scheduledTreatmentListButton);
		
		JButton logoutButton = new JButton("Log out");
		add(logoutButton);
		
		
		logoutButton.addActionListener(e -> {
			this.userManager.logout();
			dispose();
			MainFrame mainFrame = new MainFrame(userManager);
			mainFrame.setVisible(true);
		});
	}
}
