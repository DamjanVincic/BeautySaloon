package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import entity.Client;
import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

import model.ClientScheduledTreatmentModel;

public class ClientScheduledTreatmentsModal extends JDialog {
	private static final long serialVersionUID = -3627961294131643414L;
	
	private ManagerFactory managerFactory;

	public ClientScheduledTreatmentsModal(ManagerFactory managerFactory) {
		this.managerFactory = managerFactory;
		
		setTitle("Your treatments");
		setResizable(false);
		setSize(800, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]"));
		
//		add(new JLabel(""));
		JTable table = new JTable(new ClientScheduledTreatmentModel(this.managerFactory.getScheduledTreatmentManager(), (Client) managerFactory.getUserManager().getCurrentUser()));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 200));
		add(scrollPane);
		
		JButton cancelTreatmentButton = new JButton("Cancel treatment");
		add(cancelTreatmentButton);
	}
}
