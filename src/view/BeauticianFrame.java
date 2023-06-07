package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class BeauticianFrame extends JFrame {
	private static final long serialVersionUID = -8116207369997294127L;

	public BeauticianFrame(ManagerFactory managerFactory) {
		setTitle("Beautician Panel");
		setResizable(false);
		setSize(400, 275);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[][]30[][]30[]"));
		
		add(new JLabel(String.format("Welcome, %s!", managerFactory.getUserManager().getCurrentUser().getUsername())));
		
		JButton scheduleButton = new JButton("Schedule");
		add(scheduleButton);
		
		JButton completedTreatmentsButton = new JButton("Completed treatments");
		add(completedTreatmentsButton);
		
		JButton logOutButton = new JButton("Log out");
		add(logOutButton);
		
		
		scheduleButton.addActionListener(e -> {
			BeauticianScheduleDialog beauticianScheduleDialog = new BeauticianScheduleDialog(managerFactory);
			beauticianScheduleDialog.setVisible(true);
		});
		
		logOutButton.addActionListener(e -> {
			managerFactory.getUserManager().logout();
			dispose();
			MainFrame mainFrame = new MainFrame(managerFactory);
			mainFrame.setVisible(true);
		});
	}
}
