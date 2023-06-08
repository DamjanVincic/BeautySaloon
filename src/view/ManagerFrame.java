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
		setLayout(new MigLayout("wrap", "[grow, center]", "20[]20[][]20[][]20[][]40[]"));
		
		add(new JLabel(String.format("Welcome, %s!", managerFactory.getUserManager().getCurrentUser().getUsername())));
		
		JButton usersButton = new JButton("Users");
		add(usersButton);
		
		JButton saloonButton = new JButton("Saloon");
		add(saloonButton);
		
		JButton treatmentTypesButton = new JButton("Treatment types");
		add(treatmentTypesButton);
		
		JButton servicesButton = new JButton("Services");
		add(servicesButton);
		
		JButton revenueExpensesButton = new JButton("Revenues and expenses");
		add(revenueExpensesButton);
		
		JButton reportsButton = new JButton("Reports");
		add(reportsButton);
		
		JButton logOutButton = new JButton("Log out");
		add(logOutButton);
		
		
		usersButton.addActionListener(e -> {
			UsersDialog usersDialog = new UsersDialog(managerFactory);
			usersDialog.setVisible(true);
		});
		
		logOutButton.addActionListener(e -> {
			managerFactory.getUserManager().logout();
			dispose();
			MainFrame mainFrame = new MainFrame(managerFactory);
			mainFrame.setVisible(true);
		});
	}
}
