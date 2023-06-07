package view;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import entity.Client;
import entity.ScheduledTreatment;
import entity.State;
import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

import model.ClientScheduledTreatmentModel;

public class ClientScheduledTreatmentsModal extends JDialog {
	private static final long serialVersionUID = -3627961294131643414L;
	
	private ManagerFactory managerFactory;

	public ClientScheduledTreatmentsModal(JFrame parent, ManagerFactory managerFactory) {
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
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(800, 200));
		add(scrollPane);
		
		JButton goBackbutton = new JButton("Go Back");
		add(goBackbutton, "split 2");
		
		JButton cancelTreatmentButton = new JButton("Cancel treatment");
		getRootPane().setDefaultButton(cancelTreatmentButton);
		add(cancelTreatmentButton);
		
		goBackbutton.addActionListener(e -> {
			dispose();
		});
		
		cancelTreatmentButton.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row != -1) {
				ScheduledTreatment scheduledTreatment = ((ClientScheduledTreatmentModel)table.getModel()).getScheduledTreatment(row);
				if (scheduledTreatment.getState() != State.SCHEDULED) {
					JOptionPane.showMessageDialog(null, "You can only cancel scheduled treatments.", "", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the treatment?", "Cancel confirmation", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					this.managerFactory.getScheduledTreatmentManager().cancelTreatment(scheduledTreatment.getId(), this.managerFactory.getUserManager().getCurrentUser().getId());
					((ClientScheduledTreatmentModel) table.getModel()).fireTableDataChanged();
				}
				
			} else {
				JOptionPane.showMessageDialog(null, "You must select a treatment.", "Invalid selection", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosed(WindowEvent e) {
				((ClientFrame)parent).updateClientAmountSpentLabel();
            }
		});
	}
}
