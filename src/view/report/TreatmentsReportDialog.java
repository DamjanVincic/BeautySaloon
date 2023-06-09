package view.report;

import java.time.LocalDate;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.TreatmentReportModel;
import net.miginfocom.swing.MigLayout;

public class TreatmentsReportDialog extends JDialog {
	private static final long serialVersionUID = 8061063118899230848L;

	public TreatmentsReportDialog(ManagerFactory managerFactory, LocalDate startDate, LocalDate endDate) {
		setTitle("Treatments Report");
		setResizable(false);
		setSize(600, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JTable treatmentReportTable = new JTable(new TreatmentReportModel(managerFactory.getScheduledTreatmentManager().scheduledTreatmentsStateReport(startDate, endDate)));
		JScrollPane scrollPane = new JScrollPane(treatmentReportTable);
		add(scrollPane);
	}
}
