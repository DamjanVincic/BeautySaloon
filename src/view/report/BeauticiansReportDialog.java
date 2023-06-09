package view.report;

import java.time.LocalDate;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.BeauticiansReportModel;
import net.miginfocom.swing.MigLayout;

public class BeauticiansReportDialog extends JDialog {
	private static final long serialVersionUID = 63478006362713821L;

	public BeauticiansReportDialog(ManagerFactory managerFactory, LocalDate startDate, LocalDate endDate) {
		setTitle("Beauticians Report");
		setResizable(false);
		setSize(600, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JTable beauticianReportTable = new JTable(new BeauticiansReportModel(managerFactory.getUserManager(), managerFactory.getScheduledTreatmentManager().beauticiansReport(startDate, endDate)));
		JScrollPane scrollPane = new JScrollPane(beauticianReportTable);
		add(scrollPane);
	}
	
}
