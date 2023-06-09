package view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.toedter.calendar.JDateChooser;

import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;
import view.report.BeauticiansReportDialog;
import view.report.ServicesReportDialog;
import view.report.TreatmentsReportDialog;

public class ReportsDialog extends JDialog {
	private static final long serialVersionUID = -7103095962612834562L;
	
	private LocalDate fromDate = null;
	private LocalDate toDate = null;
	
	public ReportsDialog(ManagerFactory managerFactory) {
		setTitle("Reports");
		setResizable(false);
		setSize(350, 375);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap 2", "[grow, right][grow, left]", "30[]20[]40[][][]20[]30[]"));
		
		JDateChooser fromDateChooser = new JDateChooser();
		add(new JLabel("From: "));
		add(fromDateChooser);
		
		JDateChooser toDateChooser = new JDateChooser();
		add(new JLabel("To: "));
		add(toDateChooser);
		
		JButton beauticianReports = new JButton("Beautician Reports");
		add(beauticianReports, "span, center");
		
		JButton treatmentReports = new JButton("Treatment Reports");
		add(treatmentReports, "span, center");
		
		JButton serviceReports = new JButton("Service Reports");
		add(serviceReports, "span, center");
		
		JButton loyaltyCardEligiblity = new JButton("Loyalty Card Eligibility");
		add(loyaltyCardEligiblity, "span, center");
		
		JButton goBackButton = new JButton("Go Back");
		add(goBackButton, "span, center");
		
		
		fromDateChooser.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("date")) {
                Date selectedDate = (Date) e.getNewValue();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.fromDate = localDate;
            }
		});
		
		toDateChooser.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("date")) {
                Date selectedDate = (Date) e.getNewValue();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.toDate = localDate;
            }
		});
		
		beauticianReports.addActionListener(e -> {
			if (!validateDates())
				return;
			BeauticiansReportDialog beauticiansReport = new BeauticiansReportDialog(managerFactory, fromDate, toDate);
			beauticiansReport.setVisible(true);
		});
		
		treatmentReports.addActionListener(e -> {
			if (!validateDates())
				return;
			TreatmentsReportDialog treatmentsReport = new TreatmentsReportDialog(managerFactory, fromDate, toDate);
			treatmentsReport.setVisible(true);
		});
		
		serviceReports.addActionListener(e -> {
			if (!validateDates())
				return;
			ServicesReportDialog servicesReportDialog = new ServicesReportDialog(managerFactory, fromDate, toDate);
			servicesReportDialog.setVisible(true);
		});
		
		goBackButton.addActionListener(e -> {
			dispose();
		});
	}
	
	public boolean validateDates() {
		if (this.fromDate == null || this.toDate == null) {
			JOptionPane.showMessageDialog(null, "You must select both dates.", "Error", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if (this.toDate.isBefore(fromDate)) {
			JOptionPane.showMessageDialog(null, "Invalid date input", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
