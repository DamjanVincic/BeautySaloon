package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import manage.ManagerFactory;
import model.BeauticianTreatmentModel;
import net.miginfocom.swing.MigLayout;

public class BeauticianCompletedTreatmentsDialog extends JDialog {
	private static final long serialVersionUID = 8589895391538104590L;

	public BeauticianCompletedTreatmentsDialog(ManagerFactory managerFactory) {
		setTitle("Completed treatments");
		setResizable(false);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[][]"));
		
		JTable completedTreatmentsTable = new JTable(new BeauticianTreatmentModel(managerFactory.getScheduledTreatmentManager(), managerFactory.getUserManager().getCurrentUser()));
		completedTreatmentsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		completedTreatmentsTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scheduleTableScrollPane = new JScrollPane(completedTreatmentsTable);
		scheduleTableScrollPane.setPreferredSize(new Dimension(800, 400));
		add(scheduleTableScrollPane);
		
		JButton goBackButton = new JButton("Go Back");
		add(goBackButton);

		
		goBackButton.addActionListener(e -> {
			dispose();
		});
	}
}
