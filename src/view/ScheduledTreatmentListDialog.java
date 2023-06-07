package view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import entity.ScheduledTreatment;
import entity.State;
import manage.ManagerFactory;
import model.ScheduledTreatmentModel;
import net.miginfocom.swing.MigLayout;

public class ScheduledTreatmentListDialog extends JDialog {
	private static final long serialVersionUID = -2892273864677984944L;
	
	public ScheduledTreatmentListDialog(ManagerFactory managerFactory) {
		setTitle("All treatments");
		setResizable(false);
		setSize(900, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]"));
		
		JTable table = new JTable(new ScheduledTreatmentModel(managerFactory.getScheduledTreatmentManager()));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(900, 200));
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
				ScheduledTreatment scheduledTreatment = ((ScheduledTreatmentModel)table.getModel()).getScheduledTreatment(row);
				if (scheduledTreatment.getState() != State.SCHEDULED) {
					JOptionPane.showMessageDialog(null, "You can only cancel scheduled treatments.", "", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel the treatment?", "Cancel confirmation", JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					managerFactory.getScheduledTreatmentManager().cancelTreatment(scheduledTreatment.getId(), managerFactory.getUserManager().getCurrentUser().getId());
					((ScheduledTreatmentModel) table.getModel()).fireTableDataChanged();
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must select a treatment.", "Invalid selection", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
}
