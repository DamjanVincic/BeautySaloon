package view;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.PieStyler;
import org.knowm.xchart.style.Styler;

import entity.State;
import manage.ManagerFactory;
import net.miginfocom.swing.MigLayout;

public class ChartsDialog extends JDialog {
	private static final long serialVersionUID = 5695278847357931592L;
	
	public ChartsDialog(ManagerFactory managerFactory) {
		setTitle("Charts");
		setResizable(false);
		setSize(350, 250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new MigLayout("wrap", "[grow, center]", "30[]20[]20[]"));
		
		JButton earningsPerTreatmentTypeButton = new JButton("Earnings per Treatment Type");
		add(earningsPerTreatmentTypeButton);
		
		JButton numberOfTreatmentsPerBeautician = new JButton("Number of treatments ber beautician");
		add(numberOfTreatmentsPerBeautician);
		
		JButton numberOfTreatmentsPerStatus = new JButton("Number of treatments per status");
		add(numberOfTreatmentsPerStatus);
		
		
		earningsPerTreatmentTypeButton.addActionListener(e -> {
			XYChart chart = new XYChartBuilder().width(800).height(600).title("Earnings per treatment type").xAxisTitle("Month").yAxisTitle("Earnings").build();
			chart.getStyler().setChartTitleVisible(true);
			chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
			
			LocalDate currentDate = LocalDate.now();

	        List<YearMonth> last12Months = new ArrayList<>();

	        for (int i = 0; i < 12; i++) {
	            YearMonth yearMonth = YearMonth.from(currentDate);
	            last12Months.add(yearMonth);

	            currentDate = currentDate.minusMonths(1);
	        }
	        Collections.reverse(last12Months);
	        
	        HashMap<Integer, ArrayList<Double>> treatmentTypesEarningsPerMonth = managerFactory.getScheduledTreatmentManager().getTreatmentTypesEarningsPerMonth(last12Months.get(0), last12Months.get(11));
	        
	        for (Map.Entry<Integer, ArrayList<Double>> entry : treatmentTypesEarningsPerMonth.entrySet()) {
	        	chart.addSeries(managerFactory.getTreatmentTypeManager().findTreatmentTypeByID(entry.getKey()).getType(), entry.getValue());
	        }
	        chart.getStyler().setxAxisTickLabelsFormattingFunction(x -> last12Months.get(x.intValue()-1).toString());
	        
	        Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
			t.start();
		});
		
		numberOfTreatmentsPerBeautician.addActionListener(e -> {
			PieChart chart = new PieChartBuilder().width(800).height(600).title("Number of treatments per beautician in the last 30 days.").build();

			chart.getStyler().setLegendVisible(true);
			chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
			
			HashMap<Integer, HashMap<String, Double>> beauticiansReport = managerFactory.getScheduledTreatmentManager().beauticiansReport(LocalDate.now().minusMonths(1), LocalDate.now());

			for (Map.Entry<Integer, HashMap<String, Double>> entry : beauticiansReport.entrySet()) {
				if (entry.getValue().get("treatmentNumber") > 0)
					chart.addSeries(managerFactory.getUserManager().findUserById(entry.getKey()).getUsername(), entry.getValue().get("treatmentNumber"));
			}
			
			Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
			t.start();
		});
		
		numberOfTreatmentsPerStatus.addActionListener(e -> {
			PieChart chart = new PieChartBuilder().width(800).height(600).title("Status of treatments in the last 30 days.").build();

			chart.getStyler().setLegendVisible(true);
			chart.getStyler().setLegendPosition(PieStyler.LegendPosition.InsideNW);
			
			HashMap<State, Integer> beauticiansReport = managerFactory.getScheduledTreatmentManager().scheduledTreatmentsStateReport(LocalDate.now().minusMonths(1), LocalDate.now());

			for (Map.Entry<State, Integer> entry : beauticiansReport.entrySet()) {
				if (entry.getValue() > 0)
					chart.addSeries(entry.getKey().getText(), entry.getValue());
			}
			
			Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
			t.start();
		});
	}
}
