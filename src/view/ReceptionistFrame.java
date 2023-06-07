package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class ReceptionistFrame extends JFrame {
	private static final long serialVersionUID = -3030396091283564291L;

	public ReceptionistFrame(ManagerFactory managerFactory) {
		setTitle("Receptionist Panel");
		setResizable(false);
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[][]20[]"));
		
		add(new JLabel(String.format("Welcome, %s!", managerFactory.getUserManager().getCurrentUser().getUsername())));
		
		JButton scheduleTreatmentButton = new JButton("Schedule Treatment");
		add(scheduleTreatmentButton);
		
		JButton allTreatmentsButton = new JButton("All Treatments");
		add(allTreatmentsButton);
		
		JButton logOutButton = new JButton("Log out");
		add(logOutButton);
		
		
		scheduleTreatmentButton.addActionListener(e -> {
			ReceptionistClientPickerDialog receptionistClientPickerDialog = new ReceptionistClientPickerDialog(managerFactory);
			receptionistClientPickerDialog.setVisible(true);
		});
		
		allTreatmentsButton.addActionListener(e -> {
			ScheduledTreatmentListDialog scheduledTreatmentListDialog = new ScheduledTreatmentListDialog(managerFactory);
			scheduledTreatmentListDialog.setVisible(true);
		});
		
		logOutButton.addActionListener(e -> {
			managerFactory.getUserManager().logout();
			dispose();
			MainFrame mainFrame = new MainFrame(managerFactory);
			mainFrame.setVisible(true);
		});
	}
}
