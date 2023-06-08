package view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import entity.Client;
import entity.Employee;
import entity.User;
import manage.ManagerFactory;
import model.ClientModel;
import model.EmployeeModel;
import net.miginfocom.swing.MigLayout;
import view.addEdit.ClientAddEditDialog;
import view.addEdit.EmployeeAddEditDialog;

public class UsersDialog extends JDialog {
	private static final long serialVersionUID = 5443140471807417339L;
	
	private boolean clients = true;
	
	private JTable usersTable;

	public UsersDialog(ManagerFactory managerFactory) {
		setTitle("Users");
		setResizable(false);
		setSize(1400, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLayout(new MigLayout("wrap", "[grow, center]", "[]20[]20[][]20[]"));
		
		JRadioButton usersRadioButton = new JRadioButton("Users");
		usersRadioButton.setSelected(true);
		JRadioButton employeesRadioButton = new JRadioButton("Employees");
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(usersRadioButton);
		buttonGroup.add(employeesRadioButton);
		
		add(usersRadioButton, "split 2");
		add(employeesRadioButton);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu actionsMenu = new JMenu("Actions");
		menuBar.add(actionsMenu);
		
		JMenuItem addMenuItem = new JMenuItem("Add");
		actionsMenu.add(addMenuItem);
		JMenuItem editMenuItem = new JMenuItem("Edit");
		actionsMenu.add(editMenuItem);
		JMenuItem deleteMenuItem = new JMenuItem("Delete");
		actionsMenu.add(deleteMenuItem);
		
		setJMenuBar(menuBar);
		
		this.usersTable = new JTable(new ClientModel(managerFactory.getUserManager()));
		this.usersTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.usersTable.getTableHeader().setReorderingAllowed(false);
		JScrollPane clientTableScrollPane = new JScrollPane(this.usersTable);
		clientTableScrollPane.setPreferredSize(new Dimension(1400, 350));
		add(clientTableScrollPane);
		
		JButton loyaltyCardButton = new JButton("Loyalty Card");
		add(loyaltyCardButton);
		JButton bonusButton = new JButton("Bonus");
		bonusButton.setVisible(false);
		add(bonusButton);
		
		
		usersRadioButton.addActionListener(e -> {
			this.clients = true;
			loyaltyCardButton.setVisible(true);
			bonusButton.setVisible(false);
			usersTable.setModel(new ClientModel(managerFactory.getUserManager()));
		});
		
		employeesRadioButton.addActionListener(e -> {
			this.clients = false;
			bonusButton.setVisible(true);
			loyaltyCardButton.setVisible(false);
			usersTable.setModel(new EmployeeModel(managerFactory.getUserManager()));
		});
		
		addMenuItem.addActionListener(e -> {
			if (this.clients) {
				ClientAddEditDialog clientAddEditDialog = new ClientAddEditDialog(managerFactory, null);
				clientAddEditDialog.setVisible(true);
			} else {
				EmployeeAddEditDialog employeeAddEditDialog = new EmployeeAddEditDialog(managerFactory, null);
				employeeAddEditDialog.setVisible(true);
			}
			updateTable();
		});
		
		editMenuItem.addActionListener(e -> {
			User user = getSelectedUser();
			if (user == null) {
				JOptionPane.showMessageDialog(null, "You must select a user.", "", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if (this.clients) {
				ClientAddEditDialog clientAddEditDialog = new ClientAddEditDialog(managerFactory, (Client)user);
				clientAddEditDialog.setVisible(true);
			}
			else {
				EmployeeAddEditDialog employeeAddEditDialog = new EmployeeAddEditDialog(managerFactory, (Employee)user);
				employeeAddEditDialog.setVisible(true);
			}
			updateTable();
		});
		
		deleteMenuItem.addActionListener(e -> {
			User user = getSelectedUser();
			if (user != null) {
				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected user?", "Delete confirmation", JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
				if (choice == JOptionPane.YES_OPTION) {
					try {
						managerFactory.getUserManager().remove(user.getId());
						updateTable();
						JOptionPane.showMessageDialog(null, "User successfully deleted.", "", JOptionPane.INFORMATION_MESSAGE);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "You must select a user.", "", JOptionPane.WARNING_MESSAGE);
			}
		});
		
		loyaltyCardButton.addActionListener(e -> {
			String input = JOptionPane.showInputDialog(null, "Threshold: ", "Loyalty Card", JOptionPane.INFORMATION_MESSAGE);
			if (input != null)
				try {
					double threshold = Double.parseDouble(input);
					managerFactory.getUserManager().setLoyaltyCardThreshold(threshold);
					updateTable();
					JOptionPane.showMessageDialog(null, "Successfully set the loyalty card threshold.", "", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "The input must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
				}
		});
		
		bonusButton.addActionListener(e -> {
			JPanel panel = new JPanel(new MigLayout("wrap 2", "[right][left]", "[][]"));
	        JLabel treatmentsCompletedThresholdLabel = new JLabel("Completed treatments threshold:");
	        JTextField treatmentsCompletedField = new JTextField(20);
	        JLabel bonusLabel = new JLabel("Bonus: ");
	        JTextField bonusInput = new JTextField(20);
	        panel.add(treatmentsCompletedThresholdLabel);
	        panel.add(treatmentsCompletedField);
	        panel.add(bonusLabel);
	        panel.add(bonusInput);

	        int option = JOptionPane.showOptionDialog(null, panel, "Input Dialog", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
	        if (option == JOptionPane.OK_OPTION) {
	        	try {
	        		int treatmentsCompletedThreshold = Integer.parseInt(treatmentsCompletedField.getText());
	        		double bonus = Double.parseDouble(bonusInput.getText());
	        		managerFactory.getUserManager().setBonusRequirement(treatmentsCompletedThreshold, bonus);
	        		updateTable();
	        		JOptionPane.showMessageDialog(null, "Successfully set bonus requirement.", "", JOptionPane.INFORMATION_MESSAGE);
	        	} catch (Exception ex) {
	        		JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
	        	}
	        }
		});
	}
	
	private User getSelectedUser() {
		User user = null;
		int row = usersTable.getSelectedRow();
		if (row != -1) {
			if (this.clients) {
				user = ((ClientModel)this.usersTable.getModel()).getClient(row);
			}
			else {
				user = ((EmployeeModel)this.usersTable.getModel()).getEmployee(row);
			}
		}
		return user;
	}
	
	private void updateTable() {
		AbstractTableModel tableModel;
		if (this.clients)
			tableModel = (ClientModel)usersTable.getModel();
		else
			tableModel = (EmployeeModel)usersTable.getModel();
		tableModel.fireTableDataChanged();
	}
}
