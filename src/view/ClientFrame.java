package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import entity.Client;
import manage.ManagerFactory;
import manage.UserManager;
import net.miginfocom.swing.MigLayout;

public class ClientFrame extends JFrame {
	private static final long serialVersionUID = 6452299993070588719L;
	
	private ManagerFactory managerFactory;
	private UserManager userManager;
	private Client currentUser;
	
	public ClientFrame(ManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
		this.userManager = managerFactory.getUserManager();
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
		
		
		scheduledTreatmentListButton.addActionListener(e -> {
			ClientScheduledTreatmentsFrame clientScheduledTreatmentsFrame = new ClientScheduledTreatmentsFrame(this.managerFactory);
			clientScheduledTreatmentsFrame.setVisible(true);
		});
		
		logoutButton.addActionListener(e -> {
			this.userManager.logout();
			dispose();
			MainFrame mainFrame = new MainFrame(this.managerFactory);
			mainFrame.setVisible(true);
		});
	}
}
