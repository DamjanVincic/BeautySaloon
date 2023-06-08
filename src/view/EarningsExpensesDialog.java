package view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class EarningsExpensesDialog extends JDialog {
	private static final long serialVersionUID = -6326193968350660439L;
	
	private double earnings = 0;
	private double expenses = 0;
	private LocalDate fromDate = null;
	private LocalDate toDate = null;

	public EarningsExpensesDialog(ManagerFactory managerFactory) {
		setTitle("Earnings & Expenses");
		setResizable(false);
		setSize(350, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap 2", "[grow, right][grow, left]", "30[]20[]40[][]40[]"));
		
		JDateChooser fromDateChooser = new JDateChooser();
		add(new JLabel("From: "));
		add(fromDateChooser);
		
		JDateChooser toDateChooser = new JDateChooser();
		add(new JLabel("To: "));
		add(toDateChooser);
		
		JLabel earningsLabel = new JLabel(String.format("Earnings: %.2f", earnings));
		add(earningsLabel, "span, center");
		
		JLabel expensesLabel = new JLabel(String.format("Expenses: %.2f", expenses));
		add(expensesLabel, "span, center");
		
		JButton goBackButton = new JButton("Go Back");
		add(goBackButton, "span, center");
		
		
		fromDateChooser.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("date")) {
                Date selectedDate = (Date) e.getNewValue();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.fromDate = localDate;
                
                if (this.fromDate != null && this.toDate != null && this.fromDate.isBefore(this.toDate)) {
                	this.earnings = managerFactory.getScheduledTreatmentManager().getEarnings(fromDate, toDate);
                	this.expenses = managerFactory.getScheduledTreatmentManager().getExpenses(fromDate, toDate);
                } else {
                	this.earnings = 0;
                	this.expenses = 0;
                }
                
                earningsLabel.setText(String.format("Earnings: %.2f", earnings));
                expensesLabel.setText(String.format("Expenses: %.2f", expenses));
            }
		});
		
		toDateChooser.addPropertyChangeListener(e -> {
			if (e.getPropertyName().equals("date")) {
                Date selectedDate = (Date) e.getNewValue();
                LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                this.toDate = localDate;
                
                if (this.fromDate != null && this.toDate != null && this.fromDate.isBefore(this.toDate)) {
                	this.earnings = managerFactory.getScheduledTreatmentManager().getEarnings(fromDate, toDate);
                	this.expenses = managerFactory.getScheduledTreatmentManager().getExpenses(fromDate, toDate);
                } else {
                	this.earnings = 0;
                	this.expenses = 0;
                }
                
                earningsLabel.setText(String.format("Earnings: %.2f", earnings));
                expensesLabel.setText(String.format("Expenses: %.2f", expenses));
            }
		});
		
		goBackButton.addActionListener(e -> {
			dispose();
		});
	}
}
