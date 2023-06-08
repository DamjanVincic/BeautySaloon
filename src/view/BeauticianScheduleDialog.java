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
import model.BeauticianScheduledTreatmentModel;
import net.miginfocom.swing.MigLayout;

public class BeauticianScheduleDialog extends JDialog {
	private static final long serialVersionUID = -8875806247873924069L;
	
	public BeauticianScheduleDialog(ManagerFactory managerFactory) {
		setTitle("Your schedule");
		setResizable(false);
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[][]"));
		
		JTable scheduleTable = new JTable(new BeauticianScheduledTreatmentModel(managerFactory.getScheduledTreatmentManager(), managerFactory.getUserManager().getCurrentUser()));
		scheduleTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scheduleTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane scheduleTableScrollPane = new JScrollPane(scheduleTable);
		scheduleTableScrollPane.setPreferredSize(new Dimension(800, 400));
		add(scheduleTableScrollPane);
		
		JButton goBackButton = new JButton("Go Back");
		add(goBackButton, "split 2");
		
		JButton changeTreatmentStateButton = new JButton("Change treatment state");
		getRootPane().setDefaultButton(changeTreatmentStateButton);
		add(changeTreatmentStateButton);
		
		
		changeTreatmentStateButton.addActionListener(e -> {
			int row = scheduleTable.getSelectedRow();
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "You must select a treatment.", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				ScheduledTreatment scheduledTreatment = ((BeauticianScheduledTreatmentModel) scheduleTable.getModel()).getScheduledTreatment(row);
				
				String[] options = {"Completed", "Didn't show up", "Cancel"};
				int choice = JOptionPane.showOptionDialog(
						null,
						"Which state do you want to change the treatment to?",
						"Treatment state change",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]
				);
				
				switch(choice) {
					case JOptionPane.YES_OPTION:
						managerFactory.getScheduledTreatmentManager().changeScheduledTreatmentState(scheduledTreatment.getId(), State.COMPLETED);
						break;
					case JOptionPane.NO_OPTION:
						managerFactory.getScheduledTreatmentManager().changeScheduledTreatmentState(scheduledTreatment.getId(), State.NOT_SHOWED_UP);
						break;
				}
				((BeauticianScheduledTreatmentModel) scheduleTable.getModel()).fireTableDataChanged();
			}
		});
		
		goBackButton.addActionListener(e -> {
			dispose();
		});
	}

}
