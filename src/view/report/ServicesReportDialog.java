package view.report;

import java.awt.Dimension;
import java.time.LocalDate;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.ServicesReportModel;
import net.miginfocom.swing.MigLayout;

public class ServicesReportDialog extends JDialog {
	private static final long serialVersionUID = 5726403366256512843L;

	public ServicesReportDialog(ManagerFactory managerFactory, LocalDate startDate, LocalDate endDate) {
		setTitle("Services Report");
		setResizable(false);
		setSize(800, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JTable servicesReportTable = new JTable(new ServicesReportModel(managerFactory.getServiceManager(), managerFactory.getServiceManager().servicesReport(startDate, endDate)));
		JScrollPane scrollPane = new JScrollPane(servicesReportTable);
		scrollPane.setPreferredSize(new Dimension(800, 200));
		add(scrollPane);
	}
}
