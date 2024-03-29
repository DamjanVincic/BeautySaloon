package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class ManagerFrame extends JFrame {
	private static final long serialVersionUID = 5603635640219611657L;

	public ManagerFrame(ManagerFactory managerFactory) {
		setTitle("Manager Panel");
		setResizable(false);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new MigLayout("wrap", "[grow, center]", "20[]20[]20[][]20[][][]40[]"));
		
		add(new JLabel(String.format("Welcome, %s!", managerFactory.getUserManager().getCurrentUser().getUsername())));
		
		JButton usersButton = new JButton("Users");
		add(usersButton);
		
		JButton treatmentTypesButton = new JButton("Treatment Types");
		add(treatmentTypesButton);
		
		JButton servicesButton = new JButton("Services");
		add(servicesButton);
		
		JButton earningsExpensesButton = new JButton("Earnings and Expenses");
		add(earningsExpensesButton);
		
		JButton reportsButton = new JButton("Reports");
		add(reportsButton);
		
		JButton chartsButton = new JButton("Charts");
		add(chartsButton);
		
		JButton logOutButton = new JButton("Log out");
		add(logOutButton);
		
		
		usersButton.addActionListener(e -> {
			UsersDialog usersDialog = new UsersDialog(managerFactory);
			usersDialog.setVisible(true);
		});
		
		treatmentTypesButton.addActionListener(e -> {
			TreatmentTypesDialog treatmentTypesDialog = new TreatmentTypesDialog(managerFactory);
			treatmentTypesDialog.setVisible(true);
		});
		
		servicesButton.addActionListener(e -> {
			ServicesDialog servicesDialog = new ServicesDialog(managerFactory);
			servicesDialog.setVisible(true);
		});
		
		earningsExpensesButton.addActionListener(e -> {
			EarningsExpensesDialog revenuesExpensesDialog = new EarningsExpensesDialog(managerFactory);
			revenuesExpensesDialog.setVisible(true);
		});
		
		reportsButton.addActionListener(e -> {
			ReportsDialog reportsDialog = new ReportsDialog(managerFactory);
			reportsDialog.setVisible(true);
		});
		
		chartsButton.addActionListener(e -> {
			ChartsDialog chartsDialog = new ChartsDialog(managerFactory);
			chartsDialog.setVisible(true);
		});
		
		logOutButton.addActionListener(e -> {
			managerFactory.getUserManager().logout();
			dispose();
			MainFrame mainFrame = new MainFrame(managerFactory);
			mainFrame.setVisible(true);
		});
	}
}
