package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import entity.Client;
import manage.ManagerFactory;
import model.ClientModel;
import net.miginfocom.swing.MigLayout;

public class ReceptionistClientPickerDialog extends JDialog {
	private static final long serialVersionUID = 5279399074552186210L;

	public ReceptionistClientPickerDialog(ManagerFactory managerFactory) {
		setTitle("Choose a client");
		setResizable(false);
		setSize(1100, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[][]"));
		
		JTable clientTable = new JTable(new ClientModel(managerFactory.getUserManager()));
		clientTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		clientTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane clientTableScrollPane = new JScrollPane(clientTable);
		clientTableScrollPane.setPreferredSize(new Dimension(1100, 400));
		add(clientTableScrollPane);
		
		JButton goBackButton = new JButton("Go Back");
		add(goBackButton, "split 2");
		
		JButton chooseClientButton = new JButton("Choose client");
		getRootPane().setDefaultButton(chooseClientButton);
		add(chooseClientButton);
		
		chooseClientButton.addActionListener(e -> {
			int row = clientTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "You must select a client.", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				Client client = ((ClientModel) clientTable.getModel()).getClient(row);
				try {
					TreatmentScheduleDialog treatmentScheduleDialog = new TreatmentScheduleDialog(this, managerFactory, client);
					treatmentScheduleDialog.setVisible(true);
				} catch (IndexOutOfBoundsException ex) {
					JOptionPane.showMessageDialog(null, "There are currently no treatment types available.", "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		goBackButton.addActionListener(e -> {
			dispose();
		});
	}
}
