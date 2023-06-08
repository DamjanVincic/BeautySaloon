package view.addEdit;

import java.awt.Label;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import entity.TreatmentType;
import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class TreatmentTypeAddEditDialog extends JDialog {
	private static final long serialVersionUID = 5095082097313287559L;

	public TreatmentTypeAddEditDialog(ManagerFactory managerFactory, TreatmentType treatmentType) {
		if (treatmentType == null)
			setTitle("Add treatment type");
		else
			setTitle("Edit treatment type");
		setResizable(false);
		setSize(400, 100);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap 2", "[grow,right][grow,left]", "[][][]"));
		
		JTextField treatmentTypeField = new JTextField(20);
		if (treatmentType != null)
			treatmentTypeField.setText(treatmentType.getType());
		add(new JLabel("Treatment Type: "));
		add(treatmentTypeField);
		
		add(new Label());
		JButton cancelButton = new JButton("Cancel");
		add(cancelButton, "split 2");
		
		JButton doneButton = new JButton("Done");
		getRootPane().setDefaultButton(doneButton);
		add(doneButton);
		
		
		doneButton.addActionListener(e -> {
			try {
				String treatmentTypeInput = treatmentTypeField.getText();
				if (treatmentTypeInput == "")
					throw new Exception("You must fill all the fields.");
				if (treatmentType == null) {
					managerFactory.getTreatmentTypeManager().add(treatmentTypeInput);
					JOptionPane.showMessageDialog(null, "Treatment type successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					managerFactory.getTreatmentTypeManager().update(treatmentType.getId(), treatmentTypeInput);
					JOptionPane.showMessageDialog(null, "Treatment type successfully edited.", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		cancelButton.addActionListener(e -> {
			dispose();
		});
	}
}
