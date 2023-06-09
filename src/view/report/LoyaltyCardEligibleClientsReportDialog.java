package view.report;


import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.LoyaltyCardEligibleClientsReportModel;
import net.miginfocom.swing.MigLayout;

public class LoyaltyCardEligibleClientsReportDialog extends JDialog {
	private static final long serialVersionUID = -1249141244949115736L;

	public LoyaltyCardEligibleClientsReportDialog(ManagerFactory managerFactory) {
		setTitle("Loyalty Card Report");
		setResizable(false);
		setSize(600, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JTable loyaltyCardEligiblityTable = new JTable(new LoyaltyCardEligibleClientsReportModel(managerFactory.getUserManager().getLoyaltyCardEligibleClients()));
		JScrollPane scrollPane = new JScrollPane(loyaltyCardEligiblityTable);
		add(scrollPane);
	}
}
